(ns templates.page-header)

(defn render-page-header []
  [:nav#navbar
    [:div#headline.headfont "Diaper Time"]
    [:div#socials.headfont
      [:a
        {:href "http://twitter.com/rgscherf"}
        [:i {:class "fa fa-twitter fa-2x"}]]
      [:a
        {:href "http://github.com/rgscherf/diapertime"}
        [:i {:class "fa fa-github fa-2x"}]]]])
