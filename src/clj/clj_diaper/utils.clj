(ns clj-diaper.utils
  (:require [clj-time.core :as time]))

(defn- calc-percentile
  [observations this-value]
  (Math/floor
    (/ (* 100 (count (filter #(> this-value %) observations)))
       (count observations))))

(defn percentile
  [metrics field percentile-name event-map]
  (let [metric-seq (metrics field)
        this-value (event-map field)]
    (assoc-in event-map [:metrics percentile-name]
      (calc-percentile metric-seq this-value))))

(defn- awake-minutes
  [event-map]
  (time/in-minutes
    (time/interval
      (event-map :attended)
      (event-map :slept))))

(defn events-for-percentiles
  [events-map]
  {:feed (sort (map :feed events-map))
   :awake (sort (map awake-minutes events-map))})
