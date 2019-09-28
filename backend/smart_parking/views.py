import json
import requests

from django.views.generic import View
from django.http import JsonResponse

from keys import GOOGLE_KEY


def get_distanse(origin, destination):
    url = (
        'https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&'
        'origins={}&destinations={}&key={}'.format(origin, destination, GOOGLE_KEY)
    )
    data = requests.get(url)
    return data["rows"][0]["elements"][0]["duration"]["value"]


class APIEndpoint(View):
    def get(self, request):
        data = json.loads(request.body.decode("utf-8"))
        result = self._do(**data)
        return JsonResponse(result, safe=False)

    def _do(self, **kwargs):
        raise NotImplementedError()


class FindParkingsEndpoint(APIEndpoint):
    def _do(self, adress=None, time=None):
        return {'a': adress, 'b': time}
