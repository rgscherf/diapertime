(ns clj-diaper.templates.base-page
  (:require
    [hiccup.page :refer [include-js include-css html5]]))

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
   (include-css "/css/font-awesome.min.css")
   (include-css "/css/style.css")])

(defn loading-page
  [is-random]
  (html5
    page-head
    [:body {:class "body-container"}
      [:script
        (str "var isTestPage = " is-random ";")]
      mount-target
      (include-js "/js/app.js")]))