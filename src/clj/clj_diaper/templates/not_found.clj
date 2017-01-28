(ns clj-diaper.templates.not-found
  (:require
    [hiccup.page :refer [include-js include-css html5]]))

(def page-head
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css "/css/font-awesome.min.css")
   (include-css "/css/style.css")])

(defn not-found
  []
  (html5
    [:div
      page-head
      [:body
        [:div.landingDiv
          {:id "app"}
          [:div#headline.headfont.landingHeader
            "Diaper Time"]
          [:div.landingSpinner
            [:p
              "Not found! " [:a {:href "/"} "Go back to the front page."]]]]]]))
