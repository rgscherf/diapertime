(ns clj-diaper.utils
  (:require [cljs.core.match :refer-macros [match]]
            [clojure.string :as string]))

(def small-font-size {:font-size "0.7em"})
(def large-font-size {:font-size "1.4em"})

;;;;;;;;;;;;;;;;
;; RANDOM STRING
(defn random-string
  []
  (string/join
    (take 25
      (repeatedly #(rand-nth "ABCDEFGHIJKLMNOPQRSTUVWXYZ")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; NUMBERS, TIME AND PERCENTILES
(defn minutes-to-hours
  [n]
  [(quot n 60)
   (mod n 60)])

(defn str-min-hours
  [n]
  (let [ min-hr (minutes-to-hours n)
         minute-string (let [minutes (second min-hr)]
                          (if (< minutes 10)
                              (str "0" minutes)
                              minutes))]
    (str (first min-hr) ":" minute-string)))

(defn percentile-suffix
  [pct]
  (cond
    (= pct 12)
    "th"
    (= pct 11)
    "th"
    (= pct 13)
    "th"
    :else
    (match [(mod pct 10)]
      [1] "st"
      [2] "nd"
      [3] "rd"
      :else "th")))

;;;;;;;;;;;;;;;;;;;;;;
;; RENDER FEEDING TEXT
(defn stringify-feed-unit
  [feed-unit]
  (match [feed-unit]
    [:ml] "mL"))

(defn round-to-ten-mls
  [num]
  (* 10 (quot num 10)))

;;;;;;;;;;;;;;;;;;;;;
;; RENDER TABLE ICONS
(defn- poop-render
  [icon isFilled id]
  ^{:key (str (if isFilled "poopIcon" "notPoopIcon") id)}
  [:i {:class (str icon (if isFilled "" " faded"))}])

(defn render-poop-icons
  ([poop-amount]
   (render-poop-icons poop-amount "2x"))
  ([poop-amount size]
   (let [not-pooped (- 3 poop-amount)
         poop-icon (str "fa fa-spacing fa-circle fa-" size)]
    [:div.tdFlexDiv
      {:style small-font-size}
      (concat
        (map (partial poop-render poop-icon true) (range poop-amount))
        (map (partial poop-render poop-icon false) (range not-pooped)))])))

(defn render-pee-icon
  ([peed?]
   (render-pee-icon peed? "2x"))
  ([peed? size]
   [:div.tdFlexDiv
    {:style small-font-size}
    [:i {:style {:text-align "center"}
         :class (if peed?
                      (str "fa fa-check fa-" size " notFaded")
                      (str "fa fa-times fa-" size " faded"))}]]))
