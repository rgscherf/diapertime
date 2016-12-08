(ns clj-diaper.models.diaper-event
  (:require [clj-diaper.db :as db]
            [monger.collection :as mcoll]))

(defn insert-event
  [event]
  ;; may want to validate here
  (mcoll/insert-and-return db/database db/event-collection event))

(defn new-event-from-args
  [baby attended pee poop feed slept]
  {:baby baby
   :attended attended
   :pee pee
   :poop poop
   :feed feed
   :slept slept})
