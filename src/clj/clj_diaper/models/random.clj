
(ns clj-diaper.models.random
  (:require [clj-time.local :as local]
            [clj-time.core :as time]
            [clj-diaper.models.model-utils :refer [random-string]]
            [monger.joda-time]))

;; RANDOM EVENT HISTORY

(defn- subtract-minutes-from
  [start-time mintime maxtime]
  (do
    (time/minus- start-time
                 (time/minutes (rand-nth (range mintime maxtime))))))

(defn- random-event-with-slept-date
  [baby prev-event]
  (let [slept-time (subtract-minutes-from (:attended prev-event)
                                          30
                                          240)]
    {:baby baby
     :attended (subtract-minutes-from slept-time 30 90)
     :pee (> (rand) 0.25)
     :poop (rand-int 4)
     :feed (rand-nth (range 80 180))
     :slept slept-time}))

(defn- reduce-through-dates
  [baby-name num-of-events]
  (reduce
    (fn [acc new]
      (if (empty? acc)
        (cons
          (random-event-with-slept-date
            baby-name
            {:attended (local/local-now)})
          acc)
        (cons
          (random-event-with-slept-date
            baby-name
            (first acc))
          acc)))
    '()
    (range num-of-events)))

(defn random-events-history
  []
  (map #(assoc % :_id (random-string 10))
    (reduce-through-dates "test baby" 50)))
