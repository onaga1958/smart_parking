import json

from django.views.generic import View
from django.http import JsonResponse

from .keys import GOOGLE_KEY
from .utils import download

parkings = {
    "City Parking": "Gessnerallee 14, 8001 Zürich",
    "Jelmoli": "Steinmühlepl. 1, 8001 Zürich",
    "Globus": "Schweizergasse 11, 8001 Zürich",
    "Urania": "Uraniastrasse 3, 8001 Zürich",
    "Talgarten": "Nüschelerstrasse 31, 8001 Zürich",
}


def get_distanse(origin, destination):
    url = (
        'https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&'
        'origins={}&destinations={}&key={}'.format(origin, destination, GOOGLE_KEY)
    )
    data = json.loads(download(url))
    return data["rows"][0]["elements"][0]["duration"]["value"]


def get_model_prediction(parking_name, time):
    return 0.5


class APIEndpoint(View):
    def get(self, request):
        data = json.loads(request.body.decode("utf-8"))
        result = self._do(**data)
        return JsonResponse(result, safe=False)

    def _do(self, **kwargs):
        raise NotImplementedError()


class FindParkingsEndpoint(APIEndpoint):
    def _do(self, adress=None, time=None):
        distance = {}
        for name, parking_adress in parkings.items():
            distance[name] = get_distanse(adress, parking_adress)
        for name, distance in sorted(distance.items(), key=lambda x: x[1]):
            occupation = get_model_prediction(name, time)
            if occupation < 0.8:
                return {"adress": parkings[name], "occupation": occupation}
