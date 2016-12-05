(ns clj-diaper.prod
  (:require [clj-diaper.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
