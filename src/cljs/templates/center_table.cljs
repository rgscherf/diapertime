(ns templates.center-table
  (:require [templates.header-row :refer [render-header-row]]
            [templates.input-row :refer [render-input-row]]
            [reagent.core :refer [atom]]
            [ajax.core :refer [GET]]
            [cljs.reader :refer [read-string]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn get-diaper-events
  [responding-atom]
  (GET "http://0.0.0.0:3449/api/1/data"
    {:handler #(reset! responding-atom (reverse (sort-by :attended %)))
     :response-format :json
     :keywords? true}))

(defn render-row
  [row-map]
  (let [{:keys [_id attended pee poop feed slept]} row-map]
    ^{:key _id}
    [:tr
      [:td attended]
      [:td (str pee)]
      [:td poop]
      [:td feed]
      [:td slept]]))

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
