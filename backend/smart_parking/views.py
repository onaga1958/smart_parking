import json
import time

from django.views.generic import View
from django.http import JsonResponse

from .keys import GOOGLE_KEY, TOMTOM_KEY
from .utils import download
from .model_prediction import get_model_prediction


class TravelModes:
    CAR = 'car'
    PEDESTRIAN = 'pedestrian'


parkings = {
    "hauscityparking": "47.3746938,8.535169",
    # "hausjelmoli": "47.3743671,8.5349368",
    # "hausglobus": "47.3751172,8.5366964",
    "hausurania": "47.374476,8.5380093",
    "haustalgarten": "47.3720928,8.5346152",
}


def get_rain(datetime):
    pass


def get_travel_time(origin, destination, travel_mode):
    url = (
        'https://api.tomtom.com/routing/1/calculateRoute/'
        '{}:{}/json?travelMode={}&key={}'.format(origin, destination, travel_mode, TOMTOM_KEY)
    )
    data = download(url)
    return data['routes'][0]['summary']['travelTimeInSeconds']


def get_driving_time(origin, destination):
    url = (
        'https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&'
        'origins={}&destinations={}&key={}'.format(origin, destination, GOOGLE_KEY)
    )
    data = download(url)
    return data["rows"][0]["elements"][0]["duration"]["value"]


def find_parking(destination, arrival_time):
    distance = {}
    for name, parking_adress in parkings.items():
        distance[name] = get_travel_time(destination, parking_adress, TravelModes.PEDESTRIAN)
    for name, distance in sorted(distance.items(), key=lambda x: x[1]):
        occupation = get_model_prediction(name, arrival_time)
        if occupation < 0.8:
            return {"adress": parkings[name], "occupation": occupation}


def calc_arrival_time_by_origin(origin, destination):
    driving_time = get_travel_time(origin, destination, TravelModes.CAR)
    current_time = time.time()
    arrival_time = current_time + driving_time + 7200 + 7200
    return time.strftime(r"%Y-%m-%d %H:%M:%S", time.localtime(arrival_time))


class FindParkingsEndpoint(View):
    def get(self, request):
        destination = request.GET['coordinates']
        splited_destination = destination.split(',')
        if len(splited_destination) > 2:
            destination = (
                '.'.join(splited_destination[0:2]) + ',' + '.'.join(splited_destination[2:4])
            )
        arrival_time = request.GET.get('arrival_time')
        origin = request.GET.get('origin')
        result = {}
        if arrival_time is not None:
            arrival_time = arrival_time.replace('T', ' ')
        else:
            arrival_time = calc_arrival_time_by_origin(origin, destination)
            result['arrival_time'] = arrival_time
        result.update(find_parking(destination, arrival_time))
        return JsonResponse(result)
