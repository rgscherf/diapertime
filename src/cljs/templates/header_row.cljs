(ns templates.header-row)

(defn render-header-row
  []
  [:thead
    [:tr
      [:th
        {:style {:min-width "155px"}}
        "Attended"]
      [:th
        {:style {:min-width "78px" :text-align "center"}}
        "Peed"]
      [:th
        {:style {:min-width "85px" :text-align "center"}}
        "Poop"]
      [:th
        {:style {:min-width "66px" :text-align "right"}}
        "Bottle"]
      [:th
        {:style {:min-width "155px"}}
        "Slept At"]]])
        
