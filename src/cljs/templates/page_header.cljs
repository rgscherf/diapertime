(ns templates.page-header)

(defn render-page-header []
  [:nav {:id "navbar"}
    [:div {:id "headline" :class "headfont"} "Diaper Time"]
    [:div {:id "socials" :class "headfont"}
      [:i {:class "fa fa-github fa-2x"}]
      [:i {:class "fa fa-twitter fa-2x"}]]])
