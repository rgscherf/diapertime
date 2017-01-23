(ns templates.summary
  (:require [reagent.core :refer [atom]]
            [clj-diaper.utils :as utils]))

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
  [desc value]
  [:div
    {:style {:flex "1"
             :display "flex"
             :flex-direction "column"}}
    [summary-box-description desc]
    [summary-box-value value]])

(defn- summary-box-container
  [{:keys [amt-slept amt-poops amt-peed amt-ate]}]
  [:div
    {:style {:display "flex"
             :font-size "0.9em"
             :padding "5px"
             :text-align "center"}}
    [summary-box "slept" (utils/str-min-hours amt-slept)]
    [summary-box "peed" (str amt-peed "x")]
    [summary-box "pooped" (str amt-poops "x")]
    [summary-box "ate" (str amt-ate "ml")]])

(defn- mk-summary-label
  [visible this-one]
  (let [this-selected (= this-one @visible)]
    [:div
      {:style {:font-size "0.9em"
               :background-color (if this-selected
                                     "#FFA8DF"
                                     "#321F47")
               :color (if this-selected
                          "#321F47"
                          "#FFA8DF")
               :font-family "'Vampiro One', cursive"
               :padding "2px"
               :padding-left "10px"
               :flex "1"
               :border-bottom "1px solid #FFA8DF"}
       :on-click #(reset! visible (if (= @visible this-one)
                                      this-one
                                      (if (= @visible :last-twenty-four)
                                          :since-midnight
                                          :last-twenty-four)))}
      (if (= this-one :last-twenty-four)
          "Last 24 hours"
          "Since midnight")]))

(defn- summary-label-div
  [visible]
  [:div
    {:style {:display "flex"}}
    [mk-summary-label visible :last-twenty-four]
    [mk-summary-label visible :since-midnight]])

(defn render-summary
  [diaper-events]
  (let [visible-summary (atom :last-twenty-four)]
    (fn []
      [:div
        {:style {:border "2px solid #FFA8DF"
                 :width "100%"}}
        [summary-label-div visible-summary]
        [summary-box-container (get-in @diaper-events [:summary @visible-summary])]])))
