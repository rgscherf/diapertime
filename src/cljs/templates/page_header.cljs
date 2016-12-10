(ns templates.page-header)

(defn render-page-header []
  [:nav#navbar
    [:div#headline.headfont "Diaper Time"]
    [:div#socials.headfont
      [:i {:class "fa fa-github fa-2x"}]
      [:i {:class "fa fa-twitter fa-2x"}]]])
      
