from flask import Flask, render_template
from pymongo import MongoClient
from bson.json_util import dumps
import datetime
import random
import os

app = Flask(__name__)

client = MongoClient()
connection_string = os.environ["MONGODB_URI"]
client = MongoClient(connection_string)
db = client['test_database']
collection = db['test_collection']


class DiaperTime():

    def __init__(self, att, s, p, pee, br, bo, sle):
        self.attended_at = att
        self.skipped_previous_feed = s
        self.poop = p
        self.pee = pee
        self.breast_feed = br
        self.bottle_feed = bo
        self.slept_at = sle

    def to_dict(self):
        return {"attendedAt": self.attended_at,
                "skippedPrevious": self.skipped_previous_feed,
                "poop": self.poop,
                "pee": self.pee,
                "breastFeed": self.breast_feed,
                "bottleFeed": self.bottle_feed,
                "sleptAt": self.slept_at}


@app.route("/genrandom")
def gen():
    for i in range(10):
        a = DiaperTime(datetime.datetime.now(),
                       random.random() > 0.5,
                       random.randrange(0, 3),
                       random.random() > 0.5,
                       random.randrange(20),
                       random.randrange(60, 120),
                       datetime.datetime.now())
        collection.insert_one(a.to_dict())
    return "operation successful"


@app.route("/")
def hello():
    all_events = [c for c in collection.find()]
    jsonified_events = dumps(all_events)
    return jsonified_events
    # return render_template("index.html", data=jsonified_events)

if __name__ == "__main__":
    app.debug = True
    app.run()
