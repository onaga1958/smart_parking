import datetime
import json
import time

from django.views.generic import View
from django.http import JsonResponse

from .keys import GOOGLE_KEY, TOMTOM_KEY
from .utils import download, FORMAT, PARKINGS
from .model_prediction import get_model_prediction


class TravelModes:
    CAR = 'car'
    PEDESTRIAN = 'pedestrian'


def bad_weather(arrival_time):
    arrival_datetime = datetime.datetime.strptime(arrival_time, FORMAT)
    cur_date = datetime.datetime.now() + datetime.timedelta(hours=2)
    prev_date = cur_date - datetime.timedelta(days=1)
    if arrival_datetime > (cur_date - datetime.timedelta(hours=12)):
        return False
    cur_date_str = datetime.datetime.strftime(cur_date, r"%Y-%m-%d")
    prev_date_str = datetime.datetime.strftime(prev_date, r"%Y-%m-%d")
    url = (
        'https://tecdottir.herokuapp.com/measurements/mythenquai?'
        'startDate={}&endDate={}'.format(prev_date_str, cur_date_str)
    )
    data = download(url)['result'][-1]['values']
    bad_precipitation = data['precipitation']['value'] > 3
    bad_temperature = data['air_temperature']['value'] < 5
    bad_wind = data['wind_speed_avg_10min']['value'] > 10
    return bad_precipitation or bad_temperature or bad_wind


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


def calc_penalty(occupation, distance, is_bad_weather):
    occupation_score = 0
    if occupation > 0.75:
        occupation_score = 120
    if occupation > 0.9:
        occupation_score = 240
    if occupation > 0.95:
        occupation_score = 480
    return distance * (1.2 if is_bad_weather else 1.0) + occupation_score


def find_parking(destination, arrival_time):
    min_penalty = 1000000000
    best_parking = None
    is_bad_weather = bad_weather(arrival_time)
    for name, parking_adress in PARKINGS.items():
        distance = get_travel_time(destination, parking_adress, TravelModes.PEDESTRIAN)
        occupation = get_model_prediction(name, arrival_time)
        penalty = calc_penalty(occupation, distance, is_bad_weather)
        if penalty < min_penalty:
            min_penalty = penalty
            best_parking = name
    return {"adress": PARKINGS[best_parking], "occupation": occupation}


def calc_arrival_time_by_origin(origin, destination, timezone_addition=7200):
    driving_time = get_travel_time(origin, destination, TravelModes.CAR)
    current_time = time.time()
    arrival_time = current_time + driving_time + timezone_addition
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
        origin = request.GET['origin']
        if arrival_time is not None:
            arrival_time = arrival_time.replace('T', ' ')
        else:
            arrival_time = calc_arrival_time_by_origin(origin, destination)
        result = find_parking(destination, arrival_time)
        parking_arrival_time = calc_arrival_time_by_origin(origin, result["adress"], 14400)
        result['arrival_time'] = parking_arrival_time
        return JsonResponse(result)
