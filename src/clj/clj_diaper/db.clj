(ns clj-diaper.db
  (:require [monger.core :as mcore]
            [monger.db :as mdb]
            [monger.collection :as mcoll]
            [monger.json]
            [clj-diaper.utils :as utils]))
  ; (:import [com.mongodb MongoOptions ServerAddress]))

(defonce db-connect
  (let [conn (mcore/connect)
        db (mcore/get-db conn "diapertime")]
    {:conn conn :db db}))

(defn db-disconnect
  [connection]
  (mcore/disconnect connection))

(let [{:keys [conn db]} db-connect]
  (def connection conn)
  (def database db))

(def user-collection "user-collection")
(def baby-collection "baby-collection")
(def event-collection "event-collection")

(defn find-all-events
  []
  (let [ all-events
          (sort-by
            :attended
            (mcoll/find-maps database event-collection))
         metrics
            (utils/events-for-percentiles all-events)]
    (->> all-events
         (pmap (partial utils/percentile metrics :feed :feed-percentile)))))
