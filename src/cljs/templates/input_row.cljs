(ns templates.input-row
  (:require [clj-diaper.utils :as utils]
            [cljs.core.match :refer-macros [match]]
            [cljs-time.local :as local]
            [cljs-time.format :as format]
            [cljs-time.core :as time]))

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

(defn insert-style
  [style-map dom-element]
  (let [[tag & children] dom-element]
    (vector
      tag
      style-map
      children)))

(defn local-time-diff
  [delta]
  (format/unparse (format/formatter "( h:mm a )")
    (time/minus-
      (local/local-now)
      (time/minutes delta))))

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
        [:div
          (new-entry-span
            (str (:attend-delta @new-event) " min ago"))
          (insert-style
            {:style {:font-size "0.9em"}}
            (new-entry-span
              (local-time-diff (:attend-delta @new-event))))]
        (btn
          #(swap! new-event assoc :attend-delta
            (- (:attend-delta @new-event) 15))
          #(or
            (<= (:attend-delta %) (:sleep-delta %))
            (= 0 (:attend-delta %)))
          "- 15 min"))

      (input-td
        (btn
          #(swap! new-event assoc :pee (not (:pee @new-event)))
          noop-handler
          "Swap!")
        (new-entry-span
          (utils/render-pee-icon (:pee @new-event) "2x"))
        [:div ; to create flexbox spacing so that pee icon will float in the middle
          {:style {:height "30px"}}])

      ;; poop
      (input-td
        (btn
          #(swap! new-event assoc :poop
            (inc (:poop @new-event)))
          #(= 3 (:poop %))
          "More")
        (new-entry-span
          (utils/render-poop-icons (:poop @new-event) "2x"))
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
          "+10")
        (new-entry-span
          (str (:feed @new-event) " " (utils/stringify-feed-unit (:feed-unit @new-event))))
        (btn
          #(swap! new-event assoc :feed
            (utils/round-to-ten-mls (max 0 (- (:feed @new-event) 10))))
          #(= 0 (:feed %))
          "-10"))

      ;; slept
      (input-td
        (btn
          #(swap! new-event assoc :sleep-delta
            (+ 15 (:sleep-delta @new-event)))
          #(<= (:attend-delta %)
               (:sleep-delta %))
          "+ 15 min")
        [:div
          (new-entry-span
            (str (:sleep-delta @new-event) " min ago"))
          (insert-style
            {:style {:font-size "0.9em"}}
            (new-entry-span
              (local-time-diff (:sleep-delta @new-event))))]
        (btn
          #(swap! new-event assoc :sleep-delta
            (- (:sleep-delta @new-event) 15))
          #(= 0 (:sleep-delta %))
          "- 15 min"))]))
