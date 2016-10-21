from flask import Flask, render_template
from flask.ext.sqlalchemy import SQLAlchemy
import os

app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = os.environ["DATABASE_URL"]
db = SQLAlchemy(app)


@app.route("/")
def hello():
    return render_template("index.html", text="I'm here!")

if __name__ == "__main__":
    app.run()
