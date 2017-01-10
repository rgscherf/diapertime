(ns templates.form-styles)

(def form-container-flex-style
  {:style {:width "351px"
           :padding "30px"
           :margin "0px auto"
           :display "flex"
           :flex-direction "column"
           :justify-content "center"
           :align-items "center"}})

(def form-field-style
  {:style {:display "flex"
           :flex-direction "column"
           :justify-content "center"
           :align-items "flex-start"
           :width "350px"
           :margin-bottom "30px"
           :color "#FFA8DF"}})

(def form-text-input-style
  {:style {:border "none"
           :border-bottom "5px solid #FFA8DF"
           :background-color "#321F47"
           :color "white"
           :width "100%"
           :padding-bottom "6px"
           :margin-bottom "6px"
           :font-size "1.8em"
           :outline "none"}})
           ; page background is 321F47

(def form-button-style
  {:style {:margin-left "10px"
           :width "70px"
           :box-shadow "none"
           :padding "5px 10px"
           :font-size "1em"}})

(def form-submit-buttons-style
  {:style {:width "349px"
           :display "flex"
           :justify-content "flex-end"
           :align-items "center"
           :margin-top "20px"}})
