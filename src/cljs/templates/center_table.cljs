(ns templates.center-table
  (:require [templates.header-row :refer [render-header-row]]
            [templates.input-row :refer [render-input-row]]
            [reagent.core :refer [atom]]
            [ajax.core :refer [GET]]
            [cljs-time.format :as format :refer [formatters]]
            [cljs-time.core :as time])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn get-diaper-events
  [responding-atom]
  (GET "http://0.0.0.0:3449/api/1/data"
    {:handler #(reset! responding-atom (reverse (sort-by :attended %)))
     :response-format :json
     :keywords? true}))

(defn format-date-from-db
  [date-string]
  (let [parsed-dt
          (format/parse (formatters :date-time-no-ms)
                        date-string)
        formatted-date
          (format/unparse (formatters :year-month-day) parsed-dt)
        formatted-time
          (format/unparse (formatters :hour-minute) parsed-dt)]
    [:div
      [:span (str formatted-time " ")]
      [:span {:class "faded"} formatted-date]]))

(defn render-row
  [row-map]
  (let [{:keys [_id attended pee poop feed slept]} row-map]
    ^{:key _id}
    [:tr
      [:td (format-date-from-db attended)]
      [:td (str pee)]
      [:td poop]
      [:td feed]
      [:td (format-date-from-db slept)]]))

(defn render-center-table
  [new-state new-event]
  (let [events (atom [])
        events-retrieve (get-diaper-events events)]
    (fn [new-state]
      [:div {:id "innerContainer"}
        [:table {:id "mainTable" :class "table table-hover"}
          [render-header-row]
          [:tbody
            ;; input row
            (if new-state
              [render-input-row new-event])
            ;; past events
            (map render-row @events)]]])))
