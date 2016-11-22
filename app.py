from flask import Flask, render_template
from pymongo import MongoClient
from bson import json_util
import json
import datetime
import random
import os

app = Flask(__name__)

client = MongoClient(os.environ["MONGODB_URI"])
db = client.get_default_database()
collection = db['diapertime']


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


@app.route("/debug-generate")
def gen():
    entries_to_generate = 10
    for i in range(entries_to_generate):
        a = DiaperTime(datetime.datetime.now(),
                       random.random() > 0.5,
                       random.randrange(0, 3),
                       random.random() > 0.5,
                       random.randrange(20),
                       random.randrange(60, 120),
                       datetime.datetime.now())
        collection.insert_one(a.to_dict())
    return "successfully generated {} new Diapertime events.".format(entries_to_generate)


@app.route("/debug-drop")
def drop():
    db.drop_collection("diapertime")
    return "successfully dropped entire Diapertime collection."


@app.route("/debug-raw")
def raw():
    all_events = [c for c in collection.find()]
    jsonified_events = json.dumps(all_events, default=json_util.default)
    return jsonified_events


@app.route("/")
def hello():
    all_events = [c for c in collection.find()]
    jsonified_events = json.dumps(all_events, default=json_util.default)
    return render_template("index2.html", data=jsonified_events)

if __name__ == "__main__":
    app.debug = True
    app.run()
