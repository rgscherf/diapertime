(ns templates.input-row
  (:require [clj-diaper.utils :as utils]
            [cljs.core.match :refer-macros [match]]))


(defn input-sandwich-helper
  [ top-button-fn
    top-button-text
    button-text
    bottom-button-fn
    bottom-button-text]
  [:td
    [:div
      {:class "newEntryTd"}
      [:button
        {:class "smallInput"
         :on-click top-button-fn}
        top-button-text]
      [:div
        {:class "newEntrySpanStyle"}
        button-text]
      [:button
        {:class "smallInput"
         :on-click bottom-button-fn}
        bottom-button-text]]])

(defn poop-map
  [poop-map-input]
  (match [poop-map-input]
    [0] "None"
    [1] "Scant"
    [2] "Normal"
    [3] "Heavy"))

(defn render-input-row
  [new-event]
  [:tr
    ;; attended
    [input-sandwich-helper
      #(swap! new-event assoc :attend-delta
        (+ 15 (:attend-delta @new-event)))
      "+ 15 min"
      (str (:attend-delta @new-event) " min ago")
      #(swap! new-event assoc :attend-delta
        (max 0 (- (:attend-delta @new-event) 15)))
      "- 15 min"]

    ;; pee
    [:td
      {:style {:display "flex" :align-items "stretch" :flex-direction "column"}}
      [:div
        {:class "newEntryTd"}
        [:button
          {:class "smallInput"
           :on-click #(swap! new-event assoc :pee (not (:pee @new-event)))}
          "Peed"]
        [:div
          {:style {:text-align "center"}}
          (if (:pee @new-event)
            "Peed!"
            "No Pee")]]]

    ;; poop
    [input-sandwich-helper
      #(swap! new-event assoc :poop
        (min 3 (inc (:poop @new-event))))
      "More"
      (poop-map (:poop @new-event))
      #(swap! new-event assoc :poop
        (max 0 (dec (:poop @new-event))))
      "Less"]

    ;; feed
    [input-sandwich-helper
      #(swap! new-event assoc :feed
        (utils/round-to-ten-mls (+ 10 (:feed @new-event))))
      "+10"
      (str (:feed @new-event) " " (utils/stringify-feed-unit (:feed-unit @new-event)))
      #(swap! new-event assoc :feed
        (utils/round-to-ten-mls (max 0 (- (:feed @new-event) 10))))
      "-10"]

    ;; slept
    [input-sandwich-helper
      #(swap! new-event assoc :sleep-delta
        ;; can't sleep before waking up
        (min (:attend-delta @new-event)
             (+ 15 (:sleep-delta @new-event))))
      "+ 15 min"
      (str (:sleep-delta @new-event) " min ago")
      #(swap! new-event assoc :sleep-delta
        (max 0
             (- (:sleep-delta @new-event) 15)))
      "- 15 min"]])
