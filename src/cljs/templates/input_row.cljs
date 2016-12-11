(ns templates.input-row
  (:require [clj-diaper.utils :as utils]
            [cljs.core.match :refer-macros [match]]))


(defn input-sandwich-helper
  "Returns a table cell of

  Button
  Text
  Button

  The caller of this function provides click handlers for both buttons.
  The buttons can also be disabled/enabled by providing predicates that
  will take an atom as the argument.

  Hint: don't need to disable the button?
  Provide #((constantly false) %) as your predicate.
  "
  [ on-click-top
    disable-top?
    top-button-text
    button-text
    on-click-bottom
    disable-bottom?
    bottom-button-text
    event-atom]
  [:td
    [:div.newEntryTd
      [:button.smallInput
        {:on-click on-click-top
         :class (if (disable-top? @event-atom) "ghostButton" "")}
        top-button-text]
      [:div.newEntrySpanStyle
        button-text]
      [:button.smallInput
        {:on-click on-click-bottom
         :class (if (disable-bottom? @event-atom) "ghostButton" "")}
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
      #((constantly false) %)
      "+ 15 min"
      (str (:attend-delta @new-event) " min ago")
      #(swap! new-event assoc :attend-delta
        (max 0 (- (:attend-delta @new-event) 15)))
      #(= 0 (:attend-delta %))
      "- 15 min"
      new-event]

    ;; pee
    [:td
      {:style {:display "flex" :align-items "stretch" :flex-direction "column"}}
      [:div.newEntryTd
        [:button.smallInput
          {:on-click #(swap! new-event assoc :pee (not (:pee @new-event)))}
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
      #(= 3 (:poop %))
      "More"
      (poop-map (:poop @new-event))
      #(swap! new-event assoc :poop
        (max 0 (dec (:poop @new-event))))
      #(= 0 (:poop %))
      "Less"
      new-event]

    ;; feed
    [input-sandwich-helper
      #(swap! new-event assoc :feed
        (utils/round-to-ten-mls (+ 10 (:feed @new-event))))
      #((constantly false) %)
      "+10"
      (str (:feed @new-event) " " (utils/stringify-feed-unit (:feed-unit @new-event)))
      #(swap! new-event assoc :feed
        (utils/round-to-ten-mls (max 0 (- (:feed @new-event) 10))))
      #(= 0 (:feed %))
      "-10"
      new-event]

    ;; slept
    [input-sandwich-helper
      #(swap! new-event assoc :sleep-delta
        ;; can't sleep before waking up
        (min (:attend-delta @new-event)
             (+ 15 (:sleep-delta @new-event))))
      #((constantly false) %)
      "+ 15 min"
      (str (:sleep-delta @new-event) " min ago")
      #(swap! new-event assoc :sleep-delta
        (max 0
             (- (:sleep-delta @new-event) 15)))
      #(= 0 (:sleep-delta %))
      "- 15 min"
      new-event]])
