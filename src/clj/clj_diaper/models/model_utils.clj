(ns clj-diaper.models.model-utils
  (:require [clj-time.local :as local]
            [clj-time.core :as time]
            [clj-diaper.models.diaper-event :as diaper]
            [monger.joda-time]))
            
;;;;;;;;;;;;;;;;;;;;;;;;
;; GENERATING DUMMY DATA
;;;;;;;;;;;;;;;;;;;;;;;;

(defn random-string
  [maxlen]
  (->>
    (range maxlen)
    (map (fn [x] (rand-nth (range 10))))
    clojure.string/join))

(defn random-date
  []
  (time/date-time
    2016
    (rand-nth (range 9 13)) ; month
    (rand-nth (range 1 31)) ; day
    (rand-nth (range 0 24)) ; hour
    (rand-nth (range 0 60)))) ; minute

(defn random-minute-delta
  [date min-minutes max-minutes]
  (let [delta-time
        (time/minutes (rand-nth (range min-minutes max-minutes)))]
    (time/plus- date delta-time)))

(defn generate-events
  [baby-name num-events]
  (dotimes [e num-events]
    (let [attended (random-date)]
      (diaper/insert-event
        {:baby baby-name
         :attended attended
         :pee (> (rand) 0.5)
         :poop (rand-int 4)
         :feed (rand-nth (range 80 160))
         :slept (random-minute-delta attended 30 90)}))))

(defn generate
  []
  (let [events (generate-events (random-string 10) 50)]
    (sort-by :attended events)))
