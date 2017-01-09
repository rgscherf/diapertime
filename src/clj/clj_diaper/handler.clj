(ns clj-diaper.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :refer [site]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]
            [cheshire.core :as cheshire]

            [clj-diaper.models.user :as user]
            [clj-diaper.models.random :as random]
            [clj-diaper.templates.base-page :as base-page]
            [clj-diaper.metrics :as metrics]

            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;; FROM DB
(defn baby-events
  [{:keys [auth-token]}]
  (cheshire/generate-string
    (if-let [user (user/get-user-by-token auth-token)]
      {:baby-name (:baby-name user)
       :events (apply vector
                (metrics/add-metrics (:events user)))}
      {:baby-name auth-token})))

(defn demo-events
  []
  (cheshire/generate-string
    {:baby-name "Test Baby"
     :events (metrics/add-metrics (random/random-events-history))}))

;; ROUTER
(defroutes routes
  (GET "/" [] (base-page/loading-page))
  (GET "/api/1/data" {params :params} (baby-events params))
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
