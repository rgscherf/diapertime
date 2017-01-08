(ns clj-diaper.models.diaper-event
  (:require [clj-diaper.db :as db]
            [monger.collection :as mcoll]))

(defn insert-event
  [name event]
  ;; may want to validate here
  (let [old-events (mcoll/find-maps db/database
                                    db/baby-collection
                                    name)]
    (mcoll/update-by-id db/database
                        db/baby-collection
                        (:_id old-events)
                        {:events (conj (:events old-events)
                                       event)})))

(defn new-event-from-args
  [baby attended pee poop feed slept]
  {:attended attended
   :pee pee
   :poop poop
   :feed feed
   :slept slept})

(defn baby-record-from-db
  "retrieve baby's record from DB"
  [name]
  (mcoll/find-one-as-map db/database db/baby-collection {:name name}))
