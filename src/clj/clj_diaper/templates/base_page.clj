(ns clj-diaper.templates.base-page
  (:require
    [hiccup.page :refer [include-js include-css html5]]))

(def mount-target
  [:div.landingDiv
    {:id "app"}
    [:div#headline.headfont.landingHeader
      "Diaper Time"]
    [:div
      [:i {:class "fa fa-refresh fa-spin fa-4x fa-fw landingSpinner"}]]])


(def page-head
  [:head
   [:title "Diaper Time"]
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css "/css/font-awesome.min.css")
   (include-css "/css/style.css")])

(defn loading-page
  []
  (html5
    page-head
    [:body {:class "body-container" :style {:height "100%"}}
      mount-target
      (include-js "/js/app.js")]))
