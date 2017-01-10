(ns clj-diaper.mainpage
  (:require [reagent.core :refer [atom]]
            [templates.page-header :refer [render-page-header]]
            [templates.sidebar :refer [render-sidebar]]
            [templates.events-table :refer [render-events-table]]
            [ajax.core :as ajax]
            [reagent.cookies :as cookies]))


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
(defonce diaper-events (atom nil))
;;;;;;;;;;;;;;;

(defn get-events
  [url responding-atom]
  (let [auth (cookies/get "auth-token")]
    (ajax/GET url
      {:handler #(reset! responding-atom %)
       :params {:auth-token auth}
       :response-format :json
       :keywords? true})))

(defn waiting-for-table
  [is-random]
  [:div
    {:style {:display "flex" :flex-direction "column" :align-items "center"}}
    [:div.topBottomSpace
      [:i {:class "fa fa-refresh fa-spin fa-4x fa-fw"}]]
    [:div.topBottomSpace
      [:h1
        (if is-random
          "Hold on, generating data..."
          "Hold on, loading data...")]]])

(defn reset-page-atoms
  []
  (do
    (reset! diaper-events)
    (reset! page-state {:new false})
    (reset! new-event event-template)))

(defn main-page-container
  [is-random]
  (do (reset-page-atoms)
      (get-events (if is-random
                      "/api/1/random"
                      "/api/1/data")
                  diaper-events)
      (js/scroll 0 0))
  (fn []
    [:div
      [render-page-header diaper-events]
      [:div
        (if (empty? @diaper-events)
            [waiting-for-table is-random]
            [:div
              [:div#outerContainer
                [render-sidebar page-state new-event event-template]
                [render-events-table diaper-events page-state new-event]]])]]))
