(ns clj-diaper.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :refer [site]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]
            [cheshire.core :as cheshire]

            [clj-diaper.models.user :as user]
            [clj-diaper.models.random :as random]
            [clj-diaper.models.events :as events]
            [clj-diaper.templates.base-page :as base-page]
            [clj-diaper.templates.not-found :as not-found]

            [ring.util.io :refer [string-input-stream]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.ssl :as ssl]
            [ring.util.response :refer [redirect]]))
;; ROUTER
(defroutes routes
  ;; home page
  (GET "/" [] (base-page/loading-page))

  ;; show initial event list
  (GET "/api/1/data" {params :params} (events/baby-events params))
  (GET "/api/1/random" [] (events/demo-events))

  ;; auth related
  (GET "/newuser" {params :params} (user/register-new-user params))
  (GET "/trypassword" {params :params} (user/try-password params))
  (GET "/tryauthtoken" {params :params} (user/try-auth-token params))

  ;; add new events
  (POST "/newevent" {req :body} (events/new-event req))
  (POST "/echo" {req :body} (events/echo-events req))

  (GET "/.well-known/acme-challenge/LDWMwYcq_-p_ugarj725xcqvuMWyTlDOBuwZ-folGE4"
       []
       "LDWMwYcq_-p_ugarj725xcqvuMWyTlDOBuwZ-folGE4.cMApjfr0I5YoCSJBXY2hrMHwIX6WnRnZZBxL9nDSODQ")

  ;; resources
  (resources "/")
  (not-found (not-found/not-found)))

(defn- enforce-ssl
  [handler]
  (if (or (env :dev)
          (env :test))
      handler
      (-> handler
          ssl/wrap-hsts
          ssl/wrap-ssl-redirect
          ssl/wrap-forwarded-scheme)))
(def app
  (-> (site routes)
      wrap-reload
      enforce-ssl
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
