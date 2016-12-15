(ns templates.events-table
  (:require [templates.header-row :refer [render-header-row]]
            [templates.input-row :refer [render-input-row]]
            [reagent.core :refer [atom]]
            [ajax.core :refer [GET]]
            [cljs-time.format :as format :refer [formatters]]
            [cljs-time.local :as local]
            [clj-diaper.utils :as utils :refer [small-font-size large-font-size]])
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
  (let [ parsed-dt (local/from-local-string date-string)
         formatted-date (format/unparse (format/formatter "MM/dd") parsed-dt)
         formatted-time (format/unparse (format/formatter "h:mm a") parsed-dt)]
    [:div
      [:span.notFaded
        {:style large-font-size}
        (str formatted-time " ")]
      [:span.faded
        {:style small-font-size}
        formatted-date]]))

(defn render-feed
  [feed-amount feed-percentile]
  [:td
    {:style {:text-align "right"}}
    [:div
      {:data-label "Fed"}
      (str (utils/round-to-ten-mls feed-amount) " mL")]
    [:div
      {:class "faded" :style small-font-size}
      [:div
        (str feed-percentile
             (utils/percentile-suffix feed-percentile)
             " percentile")]]])

(defn render-pee
  [peed?]
  [:td
    {:data-label "Peed?"}
    (utils/render-pee-icon peed?)])

(defn render-poop
  [poop]
  [:td
    {:data-label "Poop"}
    (utils/render-poop-icons poop)])

(defn render-time
  [label attended-time time-for time-percentile]
  [:td
    {:data-label label}
    (format-date-from-db attended-time)
    [:div
      {:class "faded" :style small-font-size}
      [:div
        (if (= label "Attended")
          (str "Slept for " (utils/str-min-hours time-for))
          (str "Awake for " (utils/str-min-hours time-for)))]
      [:div
        (str time-percentile
             (utils/percentile-suffix time-percentile)
             " percentile")]]])

; keys for metrics:
; feed-percentile
; awake-percentile
; awake-for
; slept-for
; slept-percentile
(defn render-row
  [row-map]
  (let [{:keys [_id attended pee poop feed slept metrics]} row-map
        {:keys [feed-percentile awake-percentile awake-for slept-for slept-percentile]} metrics]
    ^{:key _id}
    [:tr
      (render-time "Attended" attended slept-for slept-percentile)
      (render-pee pee)
      (render-poop poop)
      (render-feed feed feed-percentile)
      (render-time "Slept" slept awake-for awake-percentile)]))

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
