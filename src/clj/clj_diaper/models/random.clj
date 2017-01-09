(ns clj-diaper.models.random
  (:require [clj-time.local :as local]
            [clj-time.core :as time]
            [monger.joda-time]
            [monger.collection :as coll]
            [clj-diaper.db :as db]))

;; RANDOM EVENT HISTORY

(defn- subtract-minutes-from
  [start-time mintime maxtime]
  (do
    (time/minus- start-time
                 (time/minutes (rand-nth (range mintime maxtime))))))

(defn- random-event-with-slept-date
  [prev-event]
  (let [slept-time (subtract-minutes-from (:attended prev-event)
                                          30
                                          240)]
    {:attended (subtract-minutes-from slept-time 30 90)
     :pee (> (rand) 0.25)
     :poop (rand-int 4)
     :feed (rand-nth (range 80 180))
     :slept slept-time}))

(defn- reduce-through-dates
  [num-of-events]
  (reduce
    (fn [acc new]
      (if (empty? acc)
        (cons
          (random-event-with-slept-date
            {:attended (local/local-now)})
          acc)
        (cons
          (random-event-with-slept-date
            (first acc))
          acc)))
    '()
    (range num-of-events)))

(defn random-id
  [maxlen]
  (->>
    (range maxlen)
    (map (fn [x] (rand-nth (range 10))))
    clojure.string/join))

(defn random-events-history
  []
  (map #(assoc % :_id (random-id 10))
    (reduce-through-dates 50)))

; (coll/find-maps db/database db/baby-collection)
; (coll/find-maps db/database db/user-collection)
; (coll/find-one-as-map db/database db/baby-collection {:name "hildaa"})
; (let [hi-id (:_id (coll/find-one-as-map
;                     db/database
;                     db/babies
;                     {:baby-name "hilda"}))]
;   (coll/update-by-id db/database
;                      db/baby-collection
;                      hi-id
;                      {:name "hildaa"
;                       :events (into [] (reduce-through-dates 10))}))
