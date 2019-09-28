import json
import time

from django.views.generic import View
from django.http import JsonResponse

from .keys import GOOGLE_KEY
from .utils import download

parkings = {
    "City Parking": "47.3746938,8.535169",
    # "Jelmoli": "47.3743671,8.5349368",
    # "Globus": "47.3751172,8.5366964",
    "Urania": "47.374476,8.5380093",
    "Talgarten": "47.3720928,8.5346152",
}


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


def get_model_prediction(parking_name, arrival_time):
    return 0.5


class APIEndpoint(View):
    def get(self, request):
        data = json.loads(request.body.decode("utf-8"))
        result = self._do(**data)
        return JsonResponse(result, safe=False)

    def _do(self, **kwargs):
        raise NotImplementedError()


class FindParkingsEndpoint(APIEndpoint):
    def _find_parking(self, destination, arrival_time):
        distance = {}
        for name, parking_adress in parkings.items():
            distance[name] = get_driving_time(destination, parking_adress)
        for name, distance in sorted(distance.items(), key=lambda x: x[1]):
            occupation = get_model_prediction(name, arrival_time)
            if occupation < 0.8:
                return {"adress": parkings[name], "occupation": occupation}

    def _do(self, destination, arrival_time=None, origin=None):
        assert arrival_time is not None or origin is not None
        if arrival_time is not None:
            return self._find_parking(destination, arrival_time)
        if origin is not None:
            driving_time = get_driving_time(origin, destination)
            current_time = time.time()
            arrival_time = current_time + driving_time + 7200
            arrival_time_str = time.strftime(r"%Y-%m-%d %H:%M:%S", time.localtime(arrival_time))
            result = self._find_parking(destination, arrival_time)
            result['arrival_time'] = arrival_time_str
            return result
