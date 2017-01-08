(ns clj-diaper.db
  (:require [monger.core :as mcore]
            [monger.db :as mdb]
            [monger.collection :as mcoll]
            [monger.json]
            [monger.joda-time]))

(defonce db-connect
  (let [mongo-uri (System/getenv "DIAPERTIME_MONGO_URI")]
    (mcore/connect-via-uri mongo-uri)))

(defn db-disconnect
  [connection]
  (mcore/disconnect connection))

(let [{:keys [conn db]} db-connect]
  (def connection conn)
  (def database db))

; (def user-collection "user-collection")
; (def baby-collection "baby-collection")
(def babies "babies")
