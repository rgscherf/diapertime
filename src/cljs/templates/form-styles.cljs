(ns templates.form-styles)

(def form-container-flex-style
  {:style {:width "85%"
           :margin "60px auto"
           :display "flex"
           :flex-direction "column"
           :justify-content "center"
           :align-items "center"}})

(def form-field-style
  {:style {:display "flex"
           :flex-direction "column"
           :justify-content "center"
           :align-items "flex-start"
           :width "300px"
           :height "100px"}})

(def form-text-input-style
  {:style {:border "none"
           :border-bottom "5px solid #FFA8DF"
           :background-color "#321F47"
           :color "white"
           :padding-bottom "6px"
           :margin-bottom "6px"
           :font-size "1.5em"
           :outline "none"}})

(def form-button-style
  {:style {:margin-left "10px"
           :width "70px"
           :box-shadow "none"
           :padding "5px 10px"
           :font-size "1em"}})
