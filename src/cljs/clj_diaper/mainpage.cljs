(ns clj-diaper.mainpage
  (:require [reagent.core :refer [atom]]
            [templates.page-header :refer [render-page-header]]
            [templates.sidebar :refer [render-sidebar]]
            [templates.center-table :refer [render-center-table]]))

(defn main-page-container []
  (let [page-state (atom {:new false})
        new-event  (atom {:attend-delta 0
                          :pee false
                          :poop 0
                          :feed 1
                          :feed-unit :ml
                          :sleep-delta 0})]
    (fn []
      [:div
        [render-page-header]
        [:div {:id "outerContainer"}
          [render-sidebar page-state]
          [render-center-table (:new @page-state) new-event]]])))
          
