
(ns templates.landing-page)

(defn action-box-style
  [width]
  {:style {:width (str width "%")
           :height "200px"
           :display "flex"
           :justify-content "center"
           :align-items "center"
           :font-size "2em"}})


(defn modify-style
  [m kv]
  (update-in m [:style] merge kv))

(defn render-landing-page
  []
  [:div
    {:style {:max-width "800px"
             :margin "0px auto"}}
    [:div
      {:style {:display "flex"
               :flex-direction "column"
               :align-items "center"
               :margin-top "40px"
               :margin-bottom "20px"
               :font-size "1.1em"}}
      [:div#headline.headfont
        "Diaper Time"]
      [:div
        {:style {:margin-top "40px"
                 :font-size "1.2em"
                 :text-align "center"}}
        "Simple tracking for your baby's I/O."]]
    [:div ;; flexbox
      {:style {:display "flex"
               :justify-content "center"
               :align-items "center"
               :margin "20px 5%"}}
      [:div
        (action-box-style 40)
        "Try with test data"]
      [:div.faded
        (modify-style
          (action-box-style 20)
          {:font-style "italic"})
        "or"]
      [:div
        (action-box-style 40)
        "Log in!"]]
    [:div
      [:div
        [:a {:href "/random"} "Go to random!"]]
      [:div
        [:a {:href "/events"} "Go to events!"]]]])
