import json
import pickle
import pandas as pd
import numpy as np

from django.views.generic import View
from django.http import JsonResponse

from .keys import GOOGLE_KEY, TOMTOM_KEY
from .utils import download

parkings = {
    "hauscityparking": "Gessnerallee 14, 8001 Zürich",
    "hausjelmoli": "Steinmühlepl. 1, 8001 Zürich",
    "hausglobus": "Schweizergasse 11, 8001 Zürich",
    "hausurania": "Uraniastrasse 3, 8001 Zürich",
    "haustalgarten": "Nüschelerstrasse 31, 8001 Zürich",
}

schulferien = [['2016-02-13', '2016-02-28'],
               ['2016-04-23', '2016-05-08'],
               ['2016-07-16', '2016-08-21'],
               ['2016-10-08', '2016-10-23'],
               ['2016-12-24', '2017-01-07'],

               ['2017-04-15', '2017-04-30'],
               ['2017-07-15', '2017-08-20'],
               ['2017-10-07', '2017-10-22'],
               ['2017-12-23', '2018-01-07'],

               ['2018-04-21', '2018-05-06'],
               ['2018-07-14', '2018-08-19'],
               ['2018-10-08', '2018-10-20'],
               ['2018-12-24', '2019-01-06'],

               ['2019-04-22', '2019-05-04'],
               ['2019-07-15', '2019-08-17'],
               ['2019-10-07', '2019-10-19'],
               ['2019-12-23', '2020-01-04'],

               ['2020-04-13', '2020-05-25'],
               ['2020-07-13', '2020-08-15'],
               ['2020-10-05', '2020-10-17'],
               ['2020-12-21', '2021-01-02']]

# def get_gps_cords(adress):
# url = (
# 'https://maps.googleapis.com/maps/api/geocode/json?'
# 'address={}&key={}'.format(adress, GOOGLE_KEY)
# )
# data = download(url)
# return data['']


def get_route(origin, destination):
    pass
    # url = (
    # 'https://api.tomtom.com/routing/1/calculateRoute/{}:{}'
    # )
    # data = download(url)


def get_driving_time(origin, destination):
    url = (
        'https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&'
        'origins={}&destinations={}&key={}'.format(origin, destination, GOOGLE_KEY)
    )
    data = download(url)
    return data["rows"][0]["elements"][0]["duration"]["value"]


def culc_feiertage():
    feiertage = pd.DataFrame()
    for year in range(2016, 2021):
        url='http://www.feiertage-schweiz.ch/kalender/{}/z%EF%BF%BDrich.html'.format(year)
        html_source=pd.read_html(url)
        html_source=html_source[2]
        html_source.columns = html_source.iloc[0]
        ind=html_source[html_source["Datum"].str.contains('Legende')].index.values[0]
        html_source=html_source.iloc[1:ind-1]
        html_source["Datum"]=html_source["Datum"]+"."+str(year)
        html_source["Datum"]=pd.to_datetime(html_source.Datum)
        html_source=html_source.set_index("Datum")
        feiertage=feiertage.append(html_source)
    return feiertage


def isweihnachten(series):
    if series.month == 12:
        return 1
    else:
        return 0


def shoppingdaystonextfeiertag(df):
    diffs = []
    feiertage = culc_feiertage()
    for feiertag in feiertage.index:
        diff = np.busday_count(df.date(), feiertag.date(), weekmask='Mon Tue Wed Thu Fri Sat')
        diffs.append(diff)

    try:
        return min([d for d in diffs if d >= 0])
    except:
        return 100  # in case no holiday found


def shoppingdaysafterfeiertag(df):
    diffs = []
    feiertage = culc_feiertage()
    for feiertag in feiertage.index:
        diff = np.busday_count(feiertag.date(), df.date(), weekmask='Mon Tue Wed Thu Fri Sat')
        diffs.append(diff)

    try:
        return min([d for d in diffs if d >= 0])
    except:
        return 100  # in case no holiday found


def culc_features(time):
    featurevector = ['Weekday','Time','schoolHolidays','toHoliday','fromHoliday','Christmas']

    df_prob = pd.DataFrame(columns=[0])
    df_prob = df_prob.append([0])

    format = '%Y-%m-%d %H:%M:%S'
    df_prob['Datetime'] = pd.to_datetime(time, format=format)
    df_prob = df_prob.set_index(pd.DatetimeIndex(df_prob['Datetime']))
    df_prob['Weekday'] = df_prob.index.dayofweek

    df_prob['Time'] = df_prob.index.hour * 60.0 + df_prob.index.minute

    df_prob['schoolHolidays'] = 0
    for sf in schulferien:
        df_prob.loc[sf[0]:sf[1]] = 1

    weihnachtsseries = pd.Series(df_prob.index, name='Christmas', index=df_prob.index).apply(isweihnachten)
    df_prob['Christmas'] = weihnachtsseries

    feiertagseries = pd.Series(df_prob.index, name='Holiday', index=df_prob.index).apply(shoppingdaystonextfeiertag)
    df_prob['toHoliday'] = feiertagseries

    feiertagseries = pd.Series(df_prob.index, name='Holiday', index=df_prob.index).apply(shoppingdaysafterfeiertag)
    df_prob['fromHoliday'] = feiertagseries

    features = df_prob[featurevector].values
    return features


def get_model_prediction(parking_name, time):
    filename = './models/' + parking_name + '_model.sav'
    loaded_model = pickle.load(open(filename, 'rb'))
    features = culc_features(time)
    prediction = int(loaded_model.predict([features]))
    return prediction


class APIEndpoint(View):
    def get(self, request):
        data = json.loads(request.body.decode("utf-8"))
        result = self._do(**data)
        return JsonResponse(result, safe=False)

    def _do(self, **kwargs):
        raise NotImplementedError()


class FindParkingsEndpoint(APIEndpoint):
    def _do(self, destination, time=None, origin=None):
        distance = {}
        assert time is not None or origin is not None
        if time is not None:
            for name, parking_adress in parkings.items():
                distance[name] = get_driving_time(destination, parking_adress)
            for name, distance in sorted(distance.items(), key=lambda x: x[1]):
                occupation = get_model_prediction(name, time)
                if occupation < 0.8:
                    return {"adress": parkings[name], "occupation": occupation}
        if origin is not None:
            driving_time = get_driving_time(origin, destination)
