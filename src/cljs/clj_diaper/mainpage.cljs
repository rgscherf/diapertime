(ns clj-diaper.mainpage
  (:require [reagent.core :refer [atom]]
            [templates.page-header :refer [render-page-header]]
            [templates.sidebar :refer [render-sidebar]]
            [templates.events-table :refer [render-events-table]]))


;;;;;;;;;;;;;;;
;; STATE
(def event-template {:attend-delta 0
                     :pee false
                     :poop 0
                     :feed 0
                     :feed-unit :ml
                     :sleep-delta 0})
(defonce page-state (atom {:new false}))
(defonce new-event  (atom event-template))
;;;;;;;;;;;;;;;

(defn main-page-container
  []
  (fn []
    [:div
      [render-page-header]
      [:div#outerContainer
        [render-sidebar page-state new-event event-template]
        [render-events-table (assoc @page-state :is-test (.-isTestPage js/window)) new-event]]]))
