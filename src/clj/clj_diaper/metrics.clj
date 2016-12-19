(ns clj-diaper.metrics
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [clojure.repl :as r]))

(defn- calc-percentile
  [observations this-value]
  (Math/floor
    (/ (* 100 (count (filter #(< % this-value) observations)))
       (max 1 (count observations)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ALL PERCENTILE METRICS MUST CALL THIS
(defn- percentile
  [observations ;; map w/ lists of all observations
   metric ;; field on observations map
   field-accessor ;; how to get field from event map & compare w/ observations
   percentile-name ;; name for field to be inserted in metrics map
   event-map] ;; event being acted upon
  (let [metric-seq (observations metric)
        this-value field-accessor]
    (assoc-in event-map [:metrics percentile-name]
      (calc-percentile metric-seq this-value))))

;;;;;;;;;;;;;;;;;;;;;;;;
;; CALCULATING INTERVALS
(defn- minutes-interval
  [earlier later]
  (let [t (time/in-minutes
            (time/interval earlier later))]
    t))

(defn- awake-minutes
  [event-map]
  (minutes-interval
    (event-map :attended)
    (event-map :slept)))

;;;;;;;;;;;;;;;;;
;; SLEEP INTERVAL

(defn- interval-calc
  [acc new]
  (if (empty? acc)
    {:slept-for 0 :prev-slept (:slept new)}
    {:slept-for
      (minutes-interval
        (:prev-slept (last acc))
        (:attended new))
      :prev-slept (:slept new)}))

(defn- gather-sleep-interval
  "reducer to gather sleep lengths"
  [acc new]
  (conj acc (interval-calc acc new)))

(defn- sleep-interval-fold
  "reducer to calculate sleep percentiles
  we need to see this-event and prev-event
  which is why we use a fold instead of map.
  the call to cons should be fine for lazy seqs."
  [observations acc new]
  (if (empty? acc)
    (cons new acc)
    (let [prev-slept (:slept (first acc))
          new-att (:attended new)
          diff (minutes-interval prev-slept new-att)
          ev-with-pct (percentile observations :sleep-interval diff :slept-percentile new)
          ev-with-diff (assoc-in ev-with-pct [:metrics :slept-for] diff)]
      (cons ev-with-diff acc))))

;;;;;;;;;;;;;;;;;;;;;;
;; SUMMARY OBSERVATIONS
(defn- make-observations
  [events-map]
  {:feed (map :feed events-map)
   :awake (map awake-minutes events-map)
   :sleep-interval (map :slept-for
                    (reduce gather-sleep-interval []
                      (sort-by :attended events-map)))})

;;;;;;;;;;;;;;;;;;
;; BIG METRICS MAP
(defn add-metrics
  [all-events]
  (let [observations (make-observations all-events)]
    (->>
      all-events
      (sort-by :attended)
      (reverse)
      (map #((partial percentile observations
                :feed (:feed %) :feed-percentile) %))
      (map #((partial percentile observations
               :awake (awake-minutes %) :awake-percentile) %))
      (map #(assoc-in % [:metrics :awake-for] (awake-minutes %)))
      reverse
      (reduce (partial sleep-interval-fold observations) '()))))
