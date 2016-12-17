(ns clj-diaper.models.model-utils
  (:require [clj-time.local :as local]
            [clj-time.core :as time]
            [clj-diaper.models.diaper-event :as diaper]
            [clojure.repl :as repl]
            [monger.joda-time]))

;;;;;;;;;;;;;;;;;;;;;;;;
;; GENERATING DUMMY DATA
;;;;;;;;;;;;;;;;;;;;;;;;


;; RANDOM SLEEPING

(defn subtract-minutes-from
  [start-time mintime maxtime]
  (do
    (time/minus- start-time
                 (time/minutes (rand-nth (range mintime maxtime))))))

(defn random-event-with-slept-date
  [baby prev-event]
  (let [slept-time (:slept prev-event)]
    (do
      (proto-repl.saved-values/save 2)
      {:baby baby
       :attended (subtract-minutes-from slept-time 30 90)
       :pee (> (rand) 0.25)
       :poop (rand-int 4)
       :feed (rand-nth (range 80 180))
       :slept slept-time})))

(defn reduce-through-dates
  [baby-name num-of-events]
  (reduce
    (fn [acc new]
      (proto-repl.saved-values/save 1)
      (if (empty? acc)
        (conj
          acc
          (random-event-with-slept-date
            baby-name
            {:slept (local/local-now)}))
        (conj
          acc
          (random-event-with-slept-date
            baby-name
            (first acc)))))
    []
    (range num-of-events)))

(reduce-through-dates "hilda" 10)

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
