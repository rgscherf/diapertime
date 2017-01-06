(ns clj-diaper.templates.base-page
  (:require
    [hiccup.page :refer [include-js include-css html5]]))

(def mount-target
  [:div#app {:style {:height "100%"
                     :display "flex"
                     :align-items "center"
                     :justify-content "center"}}
    [:div {:style {:font-size "80px"
                   :font-family "Vampiro One, cursive"}}
      "Diaper Time"]])


(def page-head
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css "/css/font-awesome.min.css")
   (include-css "/css/style.min.css")])

(defn loading-page
  []
  (html5
    page-head
    [:body {:class "body-container" :style {:height "100%"}}
      mount-target
      (include-js "/js/app.js")]))
