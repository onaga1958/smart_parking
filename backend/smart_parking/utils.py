import json
import requests
import sys

from time import sleep


PARKINGS = {
    "hauscityparking": "47.375175,8.535127",
    # "hausjelmoli": "47.3743671,8.5349368",
    # "hausglobus": "47.3751172,8.5366964",
    "hausurania": "47.374120,8.541040",
    "haustalgarten": "47.372353,8.535603",
}

FORMAT = r'%Y-%m-%d %H:%M:%S'


def download(url, n_tries=2, time_to_sleep=1, time_to_sleep_multiplier=1):
    while n_tries > 0:
        resp = requests.get(url)
        if resp.status_code == 200:
            return json.loads(resp.text)
        print(
            "download error, status code {}, {} tries left".format(resp.status_code, n_tries),
            file=sys.stderr
        )
        sleep(time_to_sleep)
        n_tries -= 1
        time_to_sleep *= time_to_sleep_multiplier
    raise Exception("download failed, url: {}".format(url))
