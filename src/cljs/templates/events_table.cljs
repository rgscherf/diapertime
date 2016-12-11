(ns templates.events-table
  (:require [templates.header-row :refer [render-header-row]]
            [templates.input-row :refer [render-input-row]]
            [reagent.core :refer [atom]]
            [ajax.core :refer [GET]]
            [cljs-time.format :as format :refer [formatters]]
            [cljs-time.core :as time]
            [clj-diaper.utils :as utils])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;;;;;;;;;;;;
;; STATE
(defonce diaper-events
  (atom []))
;;;;;;;;;;;;

(defn get-diaper-events
  [responding-atom]
  (GET "http://0.0.0.0:3449/api/1/data"
    {:handler #(reset! responding-atom (reverse (sort-by :attended %)))
     :response-format :json
     :keywords? true}))
(get-diaper-events diaper-events)

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
      [:span.notFaded (str formatted-time " ")]
      [:span.faded formatted-date]]))

(defn render-date-time
  [label date-time-string]
  [:td
    {:data-label label}
    (format-date-from-db date-time-string)])

(defn render-feed
  [feed-amount]
  [:td
    {:data-label "Fed"
     :style {:text-align "right"}}
    (str (utils/round-to-ten-mls feed-amount) " mL")])

(defn render-poop
  [poop-amount]
  (let [not-pooped (- 3 poop-amount)
        poop-icon "fa fa-spacing fa-circle 3x "]
    [:td
      {:data-label "Poop"}
      [:div.tdFlexDiv
        (concat
          (repeat poop-amount [:i {:class poop-icon}])
          (repeat not-pooped  [:i {:class (str poop-icon "faded")}]))]]))

(defn render-pee
  [peed?]
  [:td
    {:data-label "Peed?"}
    [:div.tdFlexDiv
      [:i {:style {:text-align "center"}
           :class (if peed?
                        "fa fa-check 2x notFaded"
                        "fa fa-times 2x faded")}]]])

(defn render-row
  [row-map]
  (let [{:keys [_id attended pee poop feed slept]} row-map]
    ^{:key _id}
    [:tr
      (render-date-time "Attended" attended)
      (render-pee pee)
      (render-poop poop)
      (render-feed feed)
      (render-date-time "Slept" slept)]))

(defn render-events-table
  [new-state new-event]
  (fn [new-state]
    [:div#innerContainer
      [:table#mainTable.table.table-hover
        [render-header-row]
        [:tbody
          ;; input row
          (if new-state
            [render-input-row new-event])
          ;; past events
          (map render-row @diaper-events)]]]))
