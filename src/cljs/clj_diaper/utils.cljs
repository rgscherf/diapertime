(ns clj-diaper.utils
  (:require [cljs.core.match :refer-macros [match]]))

(def small-font-size {:font-size "0.7em"})
(def large-font-size {:font-size "1.4em"})

(defn minutes-to-hours
  [n]
  [(quot n 60)
   (mod n 60)])

(defn stringify-feed-unit
  [feed-unit]
  (match [feed-unit]
    [:ml] "mL"))

(defn round-to-ten-mls
  [num]
  (* 10 (quot num 10)))

(defn render-poop-icons
  ([poop-amount]
   (render-poop-icons poop-amount "2x"))
  ([poop-amount size]
   (let [not-pooped (- 3 poop-amount)
         poop-icon (str "fa fa-spacing fa-circle fa-" size)]
    [:div.tdFlexDiv
      {:style small-font-size}
      (concat
        (repeat poop-amount [:i {:class poop-icon}])
        (repeat not-pooped  [:i {:class (str poop-icon " faded")}]))])))

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
