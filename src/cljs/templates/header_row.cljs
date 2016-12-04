(ns templates.header-row)

(defn render-header-row
  []
  [:thead
    [:tr
      [:th
        {:style {:min-width "155px"}}
        "Attended"]
      [:th
        {:style {:min-width "78px"}}
        "Peed"]
      [:th
        {:style {:min-width "85px"}}
        "Poop"]
      [:th
        {:style {:min-width "66px"}}
        "Bottle"]
      [:th
        {:style {:min-width "155px"}}
        "Slept At"]]])
