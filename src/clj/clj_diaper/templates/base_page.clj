(ns clj-diaper.templates.base-page
  (:require
    [hiccup.page :refer [include-js include-css html5]]))

(def mount-target
  [:div#app {:style {:height "100%"
                     :display "flex"
                     :align-items "center"
                     :justify-content "center"}}
    [:nav#navbar
      [:div#headline.headfont
        {:style {:margin-left "20px"
                 :margin-top "30px"
                 :text-align "center"}}
        "Diaper Time"]]])

(def page-head
  [:head
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
