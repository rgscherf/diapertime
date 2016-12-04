(ns clj-diaper.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :refer [site]]
            [hiccup.page :refer [include-js include-css html5]]
            [clj-diaper.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(def page-head
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css "/css/style.css")
   (include-css "https://fonts.googleapis.com/css?family=Vampiro+One")
   (include-css "https://fonts.googleapis.com/css?family=Open+Sans")])

(defn loading-page []
  (html5
    page-head
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")
     (include-js "https://use.fontawesome.com/ade5f0bcef.js")]))


(defroutes routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page))

  (resources "/")
  (not-found "Not Found"))

(def app (site #'routes))
