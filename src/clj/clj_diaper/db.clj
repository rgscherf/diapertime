(ns clj-diaper.db
  (:require [monger.core :as mcore]
            [monger.db :as mdb]
            [monger.collection :as mcoll]))
  ; (:import [com.mongodb MongoOptions ServerAddress]))

(defonce db-connect
  (let [conn (mcore/connect)
        db (mcore/get-db conn "diapertime")]
    {:conn conn :db db}))

(defn db-disconnect
  [connection]
  (mcore/disconnect connection))

(let [{:keys [conn db]} (db-connect)]
  (def connection conn)
  (def database db))

(def user-collection "user-collection")
(def baby-collection "baby-collection")
(def event-collection "event-collection")
