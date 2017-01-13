(ns templates.sidebar
  (:require [ajax.core :as ajax]
            [reagent.cookies :as cookies]))

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
                (println post-params)
                (ajax/POST post-url
                  {:format :json
                   :response-format :json
                   :keywords? true
                   :params post-params
                   :handler (fn new-events-from-server
                               [body]
                               (swap! diaper-events assoc :events body))})
                (swap! state-atom assoc :new (not adding-new-event))
                (reset! new-event-atom event-template)))}
        "Post!"])
    (if (not adding-new-event)
      [:button.smallInput
        {:on-click
          #(do
            (swap! state-atom assoc :new (not adding-new-event))
            (reset! new-event-atom event-template))
         :style {:width "100px"
                 :height "100%"
                 :font-size "1.2em"}}
        "New event"])])

(defn- summary-box-value
  [value]
  [:div
    {:style {:flex "1"
             :font-size "1.1em"}}
    value])

(defn- summary-box-description
  [desc]
  [:div
    {:style {:flex "1"
             :font-size "0.9em"}}
    desc])

(defn- summary-box
  [value desc]
  [:div
    {:style {:flex "1"
             :display "flex"
             :flex-direction "column"}}
    [summary-box-value value]
    [summary-box-description desc]])

(defn- summary-box-container
  []
  [:div
    {:style {:display "flex"
             :font-size "0.9em"
             :padding "5px"
             :text-align "center"}}
    [summary-box "15:30" "slept"]
    [summary-box "1:23" "avg slept"]
    [summary-box "8x" "peed"]
    [summary-box "3x" "pooped"]
    [summary-box "800ml" "ate"]])

(defn- summary-label
  []
  [:div
    {:style {:font-size "0.9em"
             :background-color "#FFA8DF"
             :color "#321F47"
             :font-family "'Vampiro One', cursive"
             :padding "2px"
             :padding-left "5px"}}
    "Last 24 hours"])

(defn- summary-and-action-buttons
  [state-atom new-event-atom event-template adding-new-event diaper-events]
  [:div
    {:style {:width "100%"
             :display "flex"
             :justify-content "space-between"}}
    [:div
      {:style {:border "2px solid #FFA8DF"
               :width "100%"}}
      [summary-label]
      [summary-box-container]]
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
        "Hildabeast "]
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
