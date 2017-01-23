(ns clj-diaper.models.events
  (:require [clj-diaper.models.user :as user]
            [clj-diaper.models.random :as random]
            [clj-diaper.db :as db]
            [clj-diaper.metrics :as metrics]
            [clj-diaper.models.events-summary :as summary]
            
            [cheshire.core :as cheshire]
            [ring.util.response :as response]

            [clj-time.core :as time]
            [clj-time.format :as format]
            [clj-time.local :as local :refer [*local-formatters*]]))

;;;;;;;;;;;;;;;;;
;; INITIAL EVENTS
;;;;;;;;;;;;;;;;;

(defn baby-events
  [{:keys [auth-token]}]
  (cheshire/generate-string
    (if-let [user (user/get-user-by-token auth-token)]
      {:baby-name (:baby-name user)
       :events (apply vector
                (metrics/add-metrics (:events user)))
       :summary (summary/summarize (:events user))}
      {:baby-name auth-token})))

(defn demo-events
  "randomly generate events for the demo page"
  []
  (let [events (random/random-events-history)]
    (cheshire/generate-string
      {:baby-name "Test Baby"
       :events (metrics/add-metrics events)
       :summary (summary/summarize events)})))

;;;;;;;;;;;;;
;; NEW EVENTS
;;;;;;;;;;;;;
(def test-data
  {:auth-token nil
   :events [{:attended "2017-01-13T11:03:09.028-05:00", :pee true, :poop 3, :feed 93, :slept "2017-01-13T11:36:09.028-05:00", :_id "0566945099", :metrics {:feed-percentile 16, :awake-percentile 4, :awake-for 33, :slept-percentile 74, :slept-for 177}} {:attended "2017-01-13T06:50:09.028-05:00", :pee true, :poop 0, :feed 128, :slept "2017-01-13T08:06:09.028-05:00", :_id "1903921713", :metrics {:feed-percentile 44, :awake-percentile 76, :awake-for 76, :slept-percentile 78, :slept-for 197}} {:attended "2017-01-13T02:06:09.028-05:00", :pee true, :poop 1, :feed 165, :slept "2017-01-13T03:33:09.028-05:00", :_id "9325844834", :metrics {:feed-percentile 82, :awake-percentile 96, :awake-for 87, :slept-percentile 12, :slept-for 54}} {:attended "2017-01-13T00:29:09.028-05:00", :pee true, :poop 1, :feed 97, :slept "2017-01-13T01:12:09.028-05:00", :_id "6655895057", :metrics {:feed-percentile 20, :awake-percentile 12, :awake-for 43, :slept-percentile 48, :slept-for 130}} {:attended "2017-01-12T21:03:09.028-05:00", :pee true, :poop 1, :feed 168, :slept "2017-01-12T22:19:09.028-05:00", :_id "2596321158", :metrics {:feed-percentile 88, :awake-percentile 76, :awake-for 76, :slept-percentile 52, :slept-for 134}} {:attended "2017-01-12T17:35:09.028-05:00", :pee false, :poop 3, :feed 102, :slept "2017-01-12T18:49:09.028-05:00", :_id "5782898175", :metrics {:feed-percentile 26, :awake-percentile 70, :awake-for 74, :slept-percentile 98, :slept-for 229}} {:attended "2017-01-12T12:25:09.028-05:00", :pee true, :poop 0, :feed 159, :slept "2017-01-12T13:46:09.028-05:00", :_id "2099314659", :metrics {:feed-percentile 74, :awake-percentile 84, :awake-for 81, :slept-percentile 56, :slept-for 138}} {:attended "2017-01-12T09:28:09.028-05:00", :pee true, :poop 2, :feed 132, :slept "2017-01-12T10:07:09.028-05:00", :_id "0523964518", :metrics {:feed-percentile 46, :awake-percentile 8, :awake-for 39, :slept-percentile 96, :slept-for 225}} {:attended "2017-01-12T04:16:09.028-05:00", :pee true, :poop 2, :feed 163, :slept "2017-01-12T05:43:09.028-05:00", :_id "9634563357", :metrics {:feed-percentile 78, :awake-percentile 96, :awake-for 87, :slept-percentile 82, :slept-for 202}} {:attended "2017-01-11T23:53:09.028-05:00", :pee true, :poop 1, :feed 83, :slept "2017-01-12T00:54:09.028-05:00", :_id "3486723095", :metrics {:feed-percentile 8, :awake-percentile 42, :awake-for 61, :slept-percentile 36, :slept-for 109}} {:attended "2017-01-11T21:10:09.028-05:00", :pee false, :poop 2, :feed 135, :slept "2017-01-11T22:04:09.028-05:00", :_id "7652991417", :metrics {:feed-percentile 54, :awake-percentile 32, :awake-for 54, :slept-percentile 26, :slept-for 88}} {:attended "2017-01-11T18:57:09.028-05:00", :pee true, :poop 3, :feed 179, :slept "2017-01-11T19:42:09.028-05:00", :_id "7878316907", :metrics {:feed-percentile 96, :awake-percentile 18, :awake-for 45, :slept-percentile 84, :slept-for 206}} {:attended "2017-01-11T14:06:09.028-05:00", :pee true, :poop 1, :feed 143, :slept "2017-01-11T15:31:09.028-05:00", :_id "1264641581", :metrics {:feed-percentile 64, :awake-percentile 94, :awake-for 85, :slept-percentile 24, :slept-for 83}} {:attended "2017-01-11T11:31:09.028-05:00", :pee true, :poop 3, :feed 133, :slept "2017-01-11T12:43:09.028-05:00", :_id "1136905184", :metrics {:feed-percentile 52, :awake-percentile 64, :awake-for 72, :slept-percentile 10, :slept-for 49}} {:attended "2017-01-11T09:33:09.028-05:00", :pee true, :poop 0, :feed 166, :slept "2017-01-11T10:42:09.028-05:00", :_id "7242109339", :metrics {:feed-percentile 84, :awake-percentile 56, :awake-for 69, :slept-percentile 62, :slept-for 140}} {:attended "2017-01-11T06:30:09.028-05:00", :pee true, :poop 1, :feed 95, :slept "2017-01-11T07:13:09.028-05:00", :_id "6617752432", :metrics {:feed-percentile 18, :awake-percentile 12, :awake-for 43, :slept-percentile 2, :slept-for 30}} {:attended "2017-01-11T05:05:09.028-05:00", :pee true, :poop 3, :feed 178, :slept "2017-01-11T06:00:09.028-05:00", :_id "1485531183", :metrics {:feed-percentile 94, :awake-percentile 34, :awake-for 55, :slept-percentile 30, :slept-for 94}} {:attended "2017-01-11T02:24:09.028-05:00", :pee false, :poop 3, :feed 82, :slept "2017-01-11T03:31:09.028-05:00", :_id "7571827869", :metrics {:feed-percentile 4, :awake-percentile 50, :awake-for 67, :slept-percentile 80, :slept-for 201}} {:attended "2017-01-10T22:06:09.028-05:00", :pee true, :poop 0, :feed 175, :slept "2017-01-10T23:03:09.028-05:00", :_id "1315386766", :metrics {:feed-percentile 92, :awake-percentile 36, :awake-for 57, :slept-percentile 46, :slept-for 126}} {:attended "2017-01-10T19:16:09.028-05:00", :pee true, :poop 1, :feed 142, :slept "2017-01-10T20:00:09.028-05:00", :_id "3991164359", :metrics {:feed-percentile 62, :awake-percentile 16, :awake-for 44, :slept-percentile 86, :slept-for 210}} {:attended "2017-01-10T14:42:09.028-05:00", :pee false, :poop 0, :feed 138, :slept "2017-01-10T15:46:09.028-05:00", :_id "3857014194", :metrics {:feed-percentile 56, :awake-percentile 44, :awake-for 64, :slept-percentile 54, :slept-for 136}} {:attended "2017-01-10T11:41:09.028-05:00", :pee true, :poop 3, :feed 87, :slept "2017-01-10T12:26:09.028-05:00", :_id "1621454070", :metrics {:feed-percentile 10, :awake-percentile 18, :awake-for 45, :slept-percentile 44, :slept-for 119}} {:attended "2017-01-10T08:29:09.028-05:00", :pee false, :poop 0, :feed 132, :slept "2017-01-10T09:42:09.028-05:00", :_id "4826654269", :metrics {:feed-percentile 46, :awake-percentile 66, :awake-for 73, :slept-percentile 32, :slept-for 100}} {:attended "2017-01-10T05:51:09.028-05:00", :pee true, :poop 3, :feed 139, :slept "2017-01-10T06:49:09.028-05:00", :_id "8359444722", :metrics {:feed-percentile 60, :awake-percentile 38, :awake-for 58, :slept-percentile 2, :slept-for 30}} {:attended "2017-01-10T04:07:09.028-05:00", :pee true, :poop 0, :feed 123, :slept "2017-01-10T05:21:09.028-05:00", :_id "2715314900", :metrics {:feed-percentile 38, :awake-percentile 70, :awake-for 74, :slept-percentile 42, :slept-for 117}} {:attended "2017-01-10T01:05:09.028-05:00", :pee false, :poop 1, :feed 82, :slept "2017-01-10T02:10:09.028-05:00", :_id "1246852769", :metrics {:feed-percentile 4, :awake-percentile 46, :awake-for 65, :slept-percentile 36, :slept-for 109}} {:attended "2017-01-09T22:25:09.028-05:00", :pee true, :poop 2, :feed 158, :slept "2017-01-09T23:16:09.028-05:00", :_id "0626485790", :metrics {:feed-percentile 72, :awake-percentile 26, :awake-for 51, :slept-percentile 66, :slept-for 149}} {:attended "2017-01-09T19:26:09.028-05:00", :pee true, :poop 0, :feed 132, :slept "2017-01-09T19:56:09.028-05:00", :_id "2112238790", :metrics {:feed-percentile 46, :awake-percentile 0, :awake-for 30, :slept-percentile 94, :slept-for 222}} {:attended "2017-01-09T14:53:09.028-05:00", :pee true, :poop 1, :feed 101, :slept "2017-01-09T15:44:09.028-05:00", :_id "5244430868", :metrics {:feed-percentile 24, :awake-percentile 26, :awake-for 51, :slept-percentile 64, :slept-for 146}} {:attended "2017-01-09T11:18:09.028-05:00", :pee true, :poop 1, :feed 127, :slept "2017-01-09T12:27:09.028-05:00", :_id "6343132609", :metrics {:feed-percentile 40, :awake-percentile 56, :awake-for 69, :slept-percentile 16, :slept-for 67}} {:attended "2017-01-09T09:02:09.028-05:00", :pee true, :poop 2, :feed 145, :slept "2017-01-09T10:11:09.028-05:00", :_id "0256502505", :metrics {:feed-percentile 66, :awake-percentile 56, :awake-for 69, :slept-percentile 18, :slept-for 70}} {:attended "2017-01-09T06:31:09.028-05:00", :pee true, :poop 2, :feed 171, :slept "2017-01-09T07:52:09.028-05:00", :_id "8147893643", :metrics {:feed-percentile 90, :awake-percentile 84, :awake-for 81, :slept-percentile 56, :slept-for 138}} {:attended "2017-01-09T02:49:09.028-05:00", :pee false, :poop 0, :feed 98, :slept "2017-01-09T04:13:09.028-05:00", :_id "8188829790", :metrics {:feed-percentile 22, :awake-percentile 92, :awake-for 84, :slept-percentile 22, :slept-for 80}} {:attended "2017-01-09T00:21:09.028-05:00", :pee true, :poop 3, :feed 127, :slept "2017-01-09T01:29:09.028-05:00", :_id "7154986046", :metrics {:feed-percentile 40, :awake-percentile 52, :awake-for 68, :slept-percentile 70, :slept-for 157}} {:attended "2017-01-08T21:02:09.028-05:00", :pee true, :poop 0, :feed 166, :slept "2017-01-08T21:44:09.028-05:00", :_id "6705411688", :metrics {:feed-percentile 84, :awake-percentile 10, :awake-for 42, :slept-percentile 48, :slept-for 130}} {:attended "2017-01-08T18:05:09.028-05:00", :pee false, :poop 1, :feed 121, :slept "2017-01-08T18:52:09.028-05:00", :_id "8636785043", :metrics {:feed-percentile 34, :awake-percentile 24, :awake-for 47, :slept-percentile 6, :slept-for 42}} {:attended "2017-01-08T16:18:09.028-05:00", :pee false, :poop 3, :feed 80, :slept "2017-01-08T17:23:09.028-05:00", :_id "9253604178", :metrics {:feed-percentile 0, :awake-percentile 46, :awake-for 65, :slept-percentile 28, :slept-for 93}} {:attended "2017-01-08T13:25:09.028-05:00", :pee true, :poop 3, :feed 80, :slept "2017-01-08T14:45:09.028-05:00", :_id "2944309298", :metrics {:feed-percentile 0, :awake-percentile 82, :awake-for 80, :slept-percentile 60, :slept-for 139}} {:attended "2017-01-08T09:45:09.028-05:00", :pee true, :poop 3, :feed 122, :slept "2017-01-08T11:06:09.028-05:00", :_id "4750758720", :metrics {:feed-percentile 36, :awake-percentile 84, :awake-for 81, :slept-percentile 92, :slept-for 217}} {:attended "2017-01-08T05:10:09.028-05:00", :pee true, :poop 1, :feed 154, :slept "2017-01-08T06:08:09.028-05:00", :_id "9945561739", :metrics {:feed-percentile 70, :awake-percentile 38, :awake-for 58, :slept-percentile 90, :slept-for 216}} {:attended "2017-01-08T00:42:09.028-05:00", :pee false, :poop 0, :feed 138, :slept "2017-01-08T01:34:09.028-05:00", :_id "8265897189", :metrics {:feed-percentile 56, :awake-percentile 30, :awake-for 52, :slept-percentile 14, :slept-for 56}} {:attended "2017-01-07T22:38:09.028-05:00", :pee true, :poop 1, :feed 179, :slept "2017-01-07T23:46:09.028-05:00", :_id "5222349410", :metrics {:feed-percentile 96, :awake-percentile 52, :awake-for 68, :slept-percentile 66, :slept-for 149}} {:attended "2017-01-07T18:56:09.028-05:00", :pee false, :poop 1, :feed 163, :slept "2017-01-07T20:09:09.028-05:00", :_id "5867453114", :metrics {:feed-percentile 78, :awake-percentile 66, :awake-for 73, :slept-percentile 76, :slept-for 189}} {:attended "2017-01-07T14:26:09.028-05:00", :pee true, :poop 0, :feed 108, :slept "2017-01-07T15:47:09.028-05:00", :_id "8433281939", :metrics {:feed-percentile 30, :awake-percentile 84, :awake-for 81, :slept-percentile 72, :slept-for 158}} {:attended "2017-01-07T11:03:09.028-05:00", :pee true, :poop 3, :feed 90, :slept "2017-01-07T11:48:09.028-05:00", :_id "5881963237", :metrics {:feed-percentile 14, :awake-percentile 18, :awake-for 45, :slept-percentile 8, :slept-for 46}} {:attended "2017-01-07T09:08:09.028-05:00", :pee false, :poop 0, :feed 161, :slept "2017-01-07T10:17:09.028-05:00", :_id "8448648262", :metrics {:feed-percentile 76, :awake-percentile 56, :awake-for 69, :slept-percentile 34, :slept-for 105}} {:attended "2017-01-07T06:06:09.028-05:00", :pee true, :poop 3, :feed 87, :slept "2017-01-07T07:23:09.028-05:00", :_id "0478188561", :metrics {:feed-percentile 10, :awake-percentile 80, :awake-for 77, :slept-percentile 88, :slept-for 211}} {:attended "2017-01-07T01:58:09.028-05:00", :pee true, :poop 0, :feed 107, :slept "2017-01-07T02:35:09.028-05:00", :_id "7886787470", :metrics {:feed-percentile 28, :awake-percentile 6, :awake-for 37, :slept-percentile 20, :slept-for 73}} {:attended "2017-01-07T00:14:09.028-05:00", :pee true, :poop 1, :feed 118, :slept "2017-01-07T00:45:09.028-05:00", :_id "6336363523", :metrics {:feed-percentile 32, :awake-percentile 2, :awake-for 31, :slept-percentile 40, :slept-for 110}} {:attended "2017-01-06T21:10:09.028-05:00", :pee true, :poop 3, :feed 151, :slept "2017-01-06T22:24:09.028-05:00", :_id "3396299105", :metrics {:feed-percentile 68, :awake-percentile 70, :awake-for 74}}],
   :new-event {:attend-delta 45, :pee true, :poop 2, :feed 100, :feed-unit :ml, :sleep-delta 15}})

(defn- time-from-delta
  [delta]
  (time/minus (time/now)
              (time/minutes delta)))

(defn- new-event-from-deltas
  [{:keys [pee poop feed attend-delta sleep-delta]}]
  (let [attended-time (time-from-delta attend-delta)
        slept-time (time-from-delta sleep-delta)]
    {:attended attended-time
     :pee pee
     :poop poop
     :feed feed
     :slept slept-time
     :_id (random/random-id 10)}))

(defn- persist-event
  "Save event to DB if user is real, or nil"
  [user new-event is-real-user?]
  nil)

(defn- parse-date
  [d]
  (format/parse (format/formatters :date-time) d))

(defn date-typed-events
  [events]
  (->> events
       (map (fn [e] (update e :attended parse-date)))
       (map (fn [e] (update e :slept parse-date)))))

(defn- new-events-and-summary
  [{:keys [auth-token events new-event]} & {:keys [real-user]}]
  (let [new-event (new-event-from-deltas new-event)
        user (user/get-user-by-token auth-token)
        new-events (->> (conj (date-typed-events events)
                              new-event)
                        (map #(dissoc % :metrics))
                        metrics/add-metrics)]
    (persist-event user new-event real-user)
    {:events new-events
     :summary (summary/summarize new-events)}))


(comment
  (def da "2017-01-06T21:10:09.028-05:00")
  (format/parse (format/formatters :date-time) da)
  (new-event-from-deltas (:new-event test-data))
  (new-events-and-summary test-data :real-user true))

(defn new-event
  "add a new event from a user
  params has keys [auth-token events new-event]
  1. retrieve appropriate user from auth token
  2. add event to db
  3. conj new event to event list
  4. recalc metrics and summary
  5. deliver
  "
  [req]
  (response/response
    (new-events-and-summary req :real-user true)))

(defn echo-events
  "add a new event from the demo page"
  [req]
  (response/response
    (new-events-and-summary req :real-user false)))
