import json
import requests
import sys

from time import sleep


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
