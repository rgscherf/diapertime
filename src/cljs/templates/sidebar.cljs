(ns templates.sidebar
  (:require [ajax.core :as ajax]
            [reagent.cookies :as cookies]
            [cljs-time.core :as time]
            [cljs-time.local :as local]
            [cljs-time.format :as format]
            [clj-diaper.utils :as utils]
            [templates.summary :as summary]))

(defn- time-string-from-delta
  [delta]
  (let [now (time/minus (local/local-now)
                        (time/minutes delta))]
    (format/unparse (format/formatters :date-time)
                    now)))

(defn- temp-new-event
  "From deltas atom (given by input row), return single event map to be consed onto event list.
  Purely for optimistic rendering while we wait for the full updated events list from server."
  [{:keys [attend-delta sleep-delta pee poop feed]}]
  {:feed feed
   :poop poop
   :pee pee
   :attended (time-string-from-delta attend-delta)
   :slept (time-string-from-delta sleep-delta)
   :_id (utils/random-string)})

(defn- submit-button
  [state-atom new-event-atom event-template adding-new-event diaper-events]
  [:button.smallInput
    {:style {:width "100px"
             :height "35px"}
     :on-click
       #(let [post-url (if (:demo @state-atom)
                           "/echo"
                           "/newevent")
              post-params {:auth-token (cookies/get "auth-token")
                           :events (:events @diaper-events)
                           :new-event @new-event-atom}]
          (do
            (ajax/POST post-url
              {:format :json
               :response-format :json
               :keywords? true
               :params post-params
               :handler (fn new-events-from-server
                           [payload]
                           (reset! diaper-events payload))})
            (swap! diaper-events assoc :events (cons (temp-new-event @new-event-atom)
                                                     (:events @diaper-events)))
            (swap! state-atom assoc :new (not adding-new-event))
            (reset! new-event-atom event-template)))}
    "Post!"])

(defn- render-control-buttons
  [state-atom new-event-atom event-template adding-new-event diaper-events]
  [:div
    {:style {:display "flex"
             :flex-direction "column"
             :justify-content "flex-end"
             :align-items "center"
             :margin-left "40px"}}
    (if adding-new-event
      [:button.smallInput
        {:style {:width "100px"
                 :height "35px"
                 :margin-bottom "5px"}
         :on-click
          #(do (swap! state-atom assoc :new (not adding-new-event))
               (reset! new-event-atom event-template))}
        "Cancel"])
    (if adding-new-event
      [submit-button state-atom new-event-atom event-template adding-new-event diaper-events])
    (if (not adding-new-event)
      [:button.smallInput
        {:on-click
          #(do
            (swap! state-atom assoc :new (not adding-new-event))
            (reset! new-event-atom event-template))
         :style {:width "100px"
                 :height "100%"
                 :font-size "1.2em"
                 :min-height "80px"}}
        "New event"])])

(defn- summary-and-action-buttons
  [state-atom new-event-atom event-template adding-new-event diaper-events]
  [:div
    {:style {:width "100%"
             :display "flex"
             :justify-content "space-between"}}
    [summary/render-summary diaper-events]
    [render-control-buttons state-atom
                            new-event-atom
                            event-template
                            adding-new-event
                            diaper-events]])

(defn- render-omnibox
  [state-atom new-event-atom event-template adding-new-event diaper-events]
  [:div
    {:style {:display "flex"
             :flex-direction "column"
             :justify-content "flex-start"
             :align-items "flex-start"
             :width "100%"}}
    [:div
      {:style {:margin-bottom "5px"}}
      [:span
        {:style {:font-size "2em"}}
        (str (:baby-name @diaper-events) " ")]
      [:a {:href "/logout"
           :style {:font-size "0.9em"}}
        "logout"]]
    [summary-and-action-buttons state-atom
                                new-event-atom
                                event-template
                                adding-new-event
                                diaper-events]])


(defn render-sidebar
  [state-atom new-event-atom event-template diaper-events]
  (let [adding-new-event (:new @state-atom)]
    [:div
      {:style {:display "flex"
               :justify-content "space-between"
               :width "100%"
               :margin-bottom "40px"
               :padding-top "10px"}}
      [render-omnibox state-atom
                      new-event-atom
                      event-template
                      adding-new-event
                      diaper-events]]))
