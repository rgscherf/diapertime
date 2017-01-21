(ns templates.events-table
  (:require [templates.header-row :refer [render-header-row]]
            [templates.input-row :refer [render-input-row]]
            [reagent.core :refer [atom]]
            [cljs-time.format :as format :refer [formatters]]
            [cljs-time.local :as local]
            [cljs-time.core :as time]
            [clj-diaper.utils :as utils :refer [small-font-size large-font-size]]))

;;;;;;;;;;;;
;; STATE
;;;;;;;;;;;;

(defn format-date-from-db
  [date-string]
  (let [ parsed (local/from-local-string date-string)
         parsed-dt (time/minus parsed
                               (time/minutes (.getTimezoneOffset (js/Date.))))
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
    {:style {:text-align "right"}
     :data-label "Fed"}
    [:div
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
    (if (nil? time-for)
      [:div] ;; to cover first event, with no sleep comparator
      [:div
        {:class "faded" :style small-font-size}
        [:div
          (if (= label "Attended")
            (str "Slept for " (utils/str-min-hours time-for))
            (str "Awake for " (utils/str-min-hours time-for)))]
        [:div
          (str time-percentile
               (utils/percentile-suffix time-percentile)
               " percentile")]])])

(defn render-row
  ; keys for metrics:
  ; feed-percentile
  ; awake-percentile
  ; awake-for
  ; slept-for
  ; slept-percentile
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
  [diaper-events page-state new-event]
  [:table
    [render-header-row]
    [:tbody
      ;; input row
      (if (:new @page-state)
        [render-input-row new-event (first (:events @diaper-events))])
      ;; past events
      (map render-row (:events @diaper-events))]])
