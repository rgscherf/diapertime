(ns templates.input-row
  (:require [clj-diaper.utils :as utils]
            [cljs.core.match :refer-macros [match]]))

(defn poop-map
  [poop-map-input]
  (match [poop-map-input]
    [0] "None"
    [1] "Scant"
    [2] "Normal"
    [3] "Heavy"
    :else "ERROR"))

(def noop-handler
  #((constantly false) %))

(defn input-button
  [base-class
   data-atom
   click-handler
   disabled-predicate
   text]
  (let [disabled (disabled-predicate @data-atom)]
    [:button
      {:on-click (if (not disabled) click-handler)
       :class (str
                base-class
                " "
                (if disabled "ghostButton" ""))}
      text]))

(defn new-entry-span
  [entry-text]
  [:div.newEntrySpanStyle
    entry-text])

(defn input-td
  [& elements]
  [:td
    (into
      [:div.newEntryTd]
      elements)])

(defn render-input-row
  [new-event]
  (let [btn (partial input-button "smallInput" new-event)]
    [:tr
      ;; attended
      (input-td
        (btn
          #(swap! new-event assoc :attend-delta
            (+ 15 (:attend-delta @new-event)))
          noop-handler
          "+ 15 min")
        (new-entry-span
          (str (:attend-delta @new-event) " min ago"))
        (btn
          #(swap! new-event assoc :attend-delta
            (- (:attend-delta @new-event) 15))
          #(= 0 (:attend-delta %))
          "- 15 min"))

      ;; pee
      (let
        [ add-style
          (fn [[head tail]]
            (vector
              head
              {:style {:display "flex" :align-items "stretch" :flex-direction "column"}}
              tail))]
       (add-style
        (input-td
          (btn
            #(swap! new-event assoc :pee (not (:pee @new-event)))
            noop-handler
            "Peed")
          (new-entry-span
            (if (:pee @new-event)
              "Peed!"
              "No Pee")))))

      ;; poop
      (input-td
        (btn
          #(swap! new-event assoc :poop
            (inc (:poop @new-event)))
          #(= 3 (:poop %))
          "More")
        (new-entry-span
          (poop-map (:poop @new-event)))
        (btn
          #(swap! new-event assoc :poop
            (dec (:poop @new-event)))
          #(= 0 (:poop %))
          "Less"))

      ;; feed
      (input-td
        (btn
          #(swap! new-event assoc :feed
            (utils/round-to-ten-mls (+ 10 (:feed @new-event))))
          noop-handler
          "+ 10mL")
        (new-entry-span
          (str (:feed @new-event) " " (utils/stringify-feed-unit (:feed-unit @new-event))))
        (btn
          #(swap! new-event assoc :feed
            (utils/round-to-ten-mls (max 0 (- (:feed @new-event) 10))))
          #(= 0 (:feed %))
          "- 10mL"))

      ;; slept
      (input-td
        (btn
          #(swap! new-event assoc :sleep-delta
            (+ 15 (:sleep-delta @new-event)))
          #(<= (:attend-delta %)
               (:sleep-delta %))
          "+ 15 min")
        (new-entry-span
          (str (:sleep-delta @new-event) " min ago"))
        (btn
          #(swap! new-event assoc :sleep-delta
            (- (:sleep-delta @new-event) 15))
          #(= 0 (:sleep-delta %))
          "- 15 min"))]))
