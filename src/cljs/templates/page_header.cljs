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
