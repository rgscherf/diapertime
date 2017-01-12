(ns templates.page-header)

(defn base-header
  []
  [:nav
    [:div#headline.headfont
      {:style {:margin "30px auto"
               :width "90%"
               :text-align "center"}}
      "Diaper Time"]
    [:div]])

(defn render-page-header
  [diaper-events]
  [:nav#navbar
    [:div#headline.headfont
      {:style {:margin-left "20px"
               :margin-top "30px"}}
      "Diaper Time"]])
    ; (if (nil? @diaper-events)
    ;   [:div]
    ;   [:div#babyInfo
    ;     {:style {:display "flex"
    ;              :flex-direction "column"
    ;              :justify-content "space-around"
    ;              :align-content "center"
    ;              :margin-right "20px"
    ;              :margin-left "20px"
    ;              :text-align "center"
    ;              :padding "20px"
    ;              :margin-top "30px"}}
    ;
    ;     [:div.headfont
    ;       {:style {:font-size "1.6em"}}
    ;       (:baby-name @diaper-events)]
    ;     [:div.headfont
    ;       {:style {:font-size "0.8em"}}
    ;       [:a {:href "/logout"}
    ;         "logout"]]])])

    ; [:div#socials.headfont
    ;   [:a
    ;     {:href "http://twitter.com/rgscherf"}
    ;     [:i {:class "fa fa-twitter fa-2x"}]]
    ;   [:a
    ;     {:href "http://github.com/rgscherf/diapertime"}
    ;     [:i {:class "fa fa-github fa-2x"}]]]])
