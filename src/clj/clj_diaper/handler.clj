(ns clj-diaper.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :refer [site]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]
            [cheshire.core :as cheshire]
            [clj-diaper.db :as db]
            [clj-diaper.models.random :as random]
            [clj-diaper.templates.base-page :as base-page]
            [clj-diaper.metrics :as metrics]))

;; PAGE RENDER


;; FROM DB

(defn baby-events
  []
  (cheshire/generate-string
    (apply vector
      (metrics/add-metrics (db/events-with-metrics)))))

(defn demo-events
  []
  (cheshire/generate-string
    (metrics/add-metrics (reverse (random/random-events-history)))))

;; ROUTER

(defroutes routes
  (GET "/" [] (base-page/loading-page))
  (GET "/api/1/data" [] (baby-events))
  (GET "/api/1/random" [] (demo-events))

  (resources "/")
  (not-found "Not Found"))

(def app (site #'routes))
