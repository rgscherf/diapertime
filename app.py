from flask import Flask, render_template
from flask_sqlalchemy import SQLAlchemy
import os
import datetime
import json

app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = os.environ["DATABASE_URL"]
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = True
db = SQLAlchemy(app)


class DiaperTime(db.Model):
    __tablename__ = "diaper_time"
    id = db.Column(db.Integer, primary_key=True)
    created_at = db.Column(db.DateTime, default=datetime.datetime.now)
    skipped_previous_feed = db.Column(db.Boolean)
    poop = db.Column(db.Integer)
    pee = db.Column(db.Boolean)
    breast_feed = db.Column(db.Integer)
    bottle_feed = db.Column(db.Integer)
    attended_at = db.Column(db.DateTime)
    slept_at = db.Column(db.DateTime)

    def __init__(self, s, p, pee, br, bo, att, sle):
        poops = {"none": 0,
                 "scant": 1,
                 "normal": 2,
                 "heavy": 3}
        self.skipped_previous_feed = s
        self.poop = poops[p]
        self.pee = pee
        self.breast_feed = br
        self.bottle_feed = bo
        self.attended_at = att
        self.slept_at = sle

    def to_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}


def json_time(o):
    if isinstance(o, datetime.datetime):
        return o.isoformat()
    else:
        raise TypeError


@app.route("/dummy")
def dummy():
    dum1 = DiaperTime(False, "none", True, 20, 20,
                      datetime.datetime.now(), datetime.datetime.now())
    dum2 = DiaperTime(False, "heavy", True, 0, 60,
                      datetime.datetime.now(), datetime.datetime.now())
    db.session.add(dum1)
    db.session.add(dum2)
    db.session.commit()
    return "hello"


@app.route("/")
def hello():
    all_events = [e.to_dict() for e in DiaperTime.query.all()]
    jsonified_events = [
        json.dumps(e, sort_keys=True, default=json_time) for e in all_events]
    return render_template("index.html", data=jsonified_events)

if __name__ == "__main__":
    app.debug = True
    app.run()
