(ns templates.page-header)

(defn render-page-header
  [diaper-events]
  [:nav#navbar
    [:div#headline.headfont
      {:style {:margin-left "20px"}}
      "Diaper Time"]
    (if (nil? @diaper-events)
      [:div]
      [:div#babyInfo
        {:style {:display "flex"
                 :flex-direction "column"
                 :justify-content "space-around"
                 :align-content "center"
                 :margin-right "20px"
                 :margin-left "20px"
                 :text-align "right"}}
        [:div.headfont
          {:style {:font-size "1.2em"}}
          (:baby-name @diaper-events)]
        [:div.headfont
          {:style {:font-size "0.8em"}}
          "logout"]])])

    ; [:div#socials.headfont
    ;   [:a
    ;     {:href "http://twitter.com/rgscherf"}
    ;     [:i {:class "fa fa-twitter fa-2x"}]]
    ;   [:a
    ;     {:href "http://github.com/rgscherf/diapertime"}
    ;     [:i {:class "fa fa-github fa-2x"}]]]])
