(ns clj-diaper.utils
  (:require [cljs.core.match :refer-macros [match]]))

(defn stringify-feed-unit
  [feed-unit]
  (match [feed-unit]
    [:ml] "mL"))

(defn round-to-ten-mls
  [num]
  (* 10 (quot num 10)))
