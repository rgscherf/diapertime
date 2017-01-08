(ns clj-diaper.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :refer [site]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]
            [cheshire.core :as cheshire]

            [clj-diaper.models.diaper-event :as events]
            [clj-diaper.models.user :as user]
            [clj-diaper.models.random :as random]
            [clj-diaper.templates.base-page :as base-page]
            [clj-diaper.metrics :as metrics]

            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;; FROM DB

;; shape of this data (and demo-events) is
;; {:baby-name (string)
;;  :events (vector event-map)
(defn baby-events
  [request]
  (cheshire/generate-string
    (let [token (get-in request [:cookies "auth-token"])
          user (user/get-user-by-token token)
          baby (events/baby-record-from-db (:baby-name user))]
      {:baby-name (:name baby)
       :events (apply vector
                (metrics/add-metrics (:events baby)))})))

(defn demo-events
  []
  (cheshire/generate-string
    {:baby-name "Test Baby"
     :events (metrics/add-metrics (random/random-events-history))}))

;; ROUTER
(defroutes routes
  (GET "/" [] (base-page/loading-page))
  (GET "/api/1/data" [request] (baby-events request))
  (GET "/api/1/random" [] (demo-events))

  ;; auth related
  (GET "/newuser" {params :params} (user/register-new-user params))
  (GET "/trypassword" {params :params} (user/try-password params))
  (GET "/tryauthtoken" {params :params} (user/try-auth-token params))

  (resources "/")
  (not-found "Not Found"))

(def app
  (-> (site #'routes)
      wrap-reload
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
