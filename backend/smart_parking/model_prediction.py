import pickle
import pandas as pd
import numpy as np
from datetime import datetime
from .utils import FORMAT, PARKINGS

MODELS = {
    name: pickle.load(open('../models/' + name + '_model.sav', 'rb'))
    for name in PARKINGS
}


schulferien = [
    ['2016-02-13', '2016-02-28'],
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
    ['2020-12-21', '2021-01-02'],
]

parking_capacities = {
    "hauscityparking": 620,
    "hausjelmoli": 222,
    "hausglobus": 178,
    "hausurania": 607,
    "haustalgarten": 110,
}


def culc_feiertage():
    feiertage = pd.DataFrame()
    for year in range(2016, 2021):
        url = 'http://www.feiertage-schweiz.ch/kalender/{}/z%EF%BF%BDrich.html'.format(year)
        html_source = pd.read_html(url)
        html_source = html_source[2]
        html_source.columns = html_source.iloc[0]
        ind = html_source[html_source["Datum"].str.contains('Legende')].index.values[0]
        html_source = html_source.iloc[1:ind-1]
        html_source["Datum"] = html_source["Datum"]+"."+str(year)
        html_source["Datum"] = pd.to_datetime(html_source.Datum)
        html_source = html_source.set_index("Datum")
        feiertage = feiertage.append(html_source)
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
    except Exception:
        return 100  # in case no holiday found


def shoppingdaysafterfeiertag(df):
    diffs = []
    feiertage = culc_feiertage()
    for feiertag in feiertage.index:
        diff = np.busday_count(feiertag.date(), df.date(), weekmask='Mon Tue Wed Thu Fri Sat')
        diffs.append(diff)

    try:
        return min([d for d in diffs if d >= 0])
    except Exception:
        return 100  # in case no holiday found


def load_last_year_dataset(parking_name):
    last_year_parking_data_path = (
        'https://parkendd.de/dumps/zuerichpark' + parking_name + '-2018.csv'
    )
    last_year_parking_data = pd.read_csv(
        last_year_parking_data_path, names=['Date', 'free'],
        index_col='Date', parse_dates=True,
    )
    last_year_parking_data.sort_index(inplace=True)
    last_year_parking_data.dropna(inplace=True)
    last_year_parking_data['Occupation'] = 100.0 - (
                last_year_parking_data.free / parking_capacities[parking_name] * 100.0)
    last_year_parking_data['Occupation'] = last_year_parking_data['Occupation'].astype(int)
    return last_year_parking_data


def culc_features(parking_name, time):
    featurevector = [
        'Weekday', 'Time', 'schoolHolidays', 'toHoliday', 'fromHoliday', 'Christmas',
        'shoppingSunday', 'Month', 'Last_year',
    ]

    df_prob = pd.DataFrame(columns=[0])
    df_prob = df_prob.append([0])

    df_prob['Datetime'] = pd.to_datetime(time, format=FORMAT)
    df_prob = df_prob.set_index(pd.DatetimeIndex(df_prob['Datetime']))
    df_prob['Weekday'] = df_prob.index.dayofweek

    df_prob['Time'] = df_prob.index.hour * 60.0 + df_prob.index.minute

    df_prob['schoolHolidays'] = 0
    for sf in schulferien:
        df_prob.loc[sf[0]:sf[1]] = 1

    weihnachtsseries = pd.Series(
        df_prob.index, name='Christmas', index=df_prob.index
    ).apply(isweihnachten)
    df_prob['Christmas'] = weihnachtsseries

    feiertagseries = pd.Series(
        df_prob.index, name='Holiday', index=df_prob.index
    ).apply(shoppingdaystonextfeiertag)
    df_prob['toHoliday'] = feiertagseries

    feiertagseries = pd.Series(
        df_prob.index, name='Holiday', index=df_prob.index
    ).apply(shoppingdaysafterfeiertag)
    df_prob['fromHoliday'] = feiertagseries

    df_prob['shoppingSunday'] = 0

    df_prob['Month'] = df_prob.index.month

    last_year_parking_data = load_last_year_dataset(parking_name)
    minus_year = datetime.strptime(time, FORMAT) - pd.DateOffset(years=1)
    df_prob['Last_year'] = last_year_parking_data[
        last_year_parking_data.index >= minus_year
    ].iloc[0]['Occupation']

    features = df_prob[featurevector].values
    return features


def get_model_prediction(parking_name, time):
    model = MODELS[parking_name]
    features = culc_features(parking_name, time)
    prediction = int(model.predict(features))
    return prediction / 100.0
