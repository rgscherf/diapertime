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

            [ring.util.io :refer [string-input-stream]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))
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

  ;; resources
  (resources "/")
  (not-found "Not Found"))

(def app
  (-> (site routes)
      wrap-reload
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))


; (app {:headers ["Content-Type" "application/json"]
;       :body (string-input-stream
;               (cheshire/generate-string {:auth-token "KSDHKDHG"
;                                          :test-val "hello world"}))
;       :uri "/echo"
;       :method :post})
