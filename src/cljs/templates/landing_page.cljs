
(ns templates.landing-page)

(defn- action-box-style
  [width]
  {:style {:width (str width "%")
           :height "200px"
           :display "flex"
           :justify-content "center"
           :align-items "center"
           :font-size "2em"}})


(defn- modify-style
  [m kv]
  (update-in m [:style] merge kv))

(defn- or-box
  []
  [:div.faded
    (modify-style
      (action-box-style 20)
      {:font-style "italic"})
    "or"])

(defn- test-drive-box
  []
  [:div
    (action-box-style 40)
    [:div
      [:div
        [:a.landingLink {:href "/random"
                         :style
                          {:font-family "Vampiro One, cursive"}}
          "Test drive"]]
      [:div
        {:style {:font-size "0.75em"}}
        "With random data!"]]])

(defn- login-box
  []
  [:div
    (modify-style
      (action-box-style 40)
      {:font-family "Vampiro One, cursive"})
    [:a.landingLink {:href "/login"}
      "Log in"]])


(defn- description
  []
  (let [subhead-style
          {:style {:margin-top "40px"
                   :font-size "1.6em"
                   :text-align "center"}}
        body-style
          {:style {:margin-top "40px"
                   :font-size "1.3em"
                   :text-align "justify"
                   :max-width "70%"
                   :margin-left "auto"
                   :margin-right "auto"}}]

    [:div
      [:div
        subhead-style
        "Simple tracking for your baby's I/O."]
      [:div
        body-style
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque hendrerit, mi ut lacinia scelerisque, ligula mi hendrerit arcu, sit amet sodales lacus justo laoreet metus. Sed et justo in libero euismod euismod. Integer sit amet laoreet orci. Pellentesque mi purus, tristique sit amet turpis quis, dictum ultricies felis. Fusce ac lectus tincidunt, finibus lectus non, elementum sapien. Nam at imperdiet est, et malesuada neque. Vestibulum laoreet ligula turpis. Phasellus vel placerat lectus. Vivamus vestibulum gravida justo, non lobortis ante ornare et. Maecenas volutpat eleifend ante. Interdum et malesuada fames ac ante ipsum primis in faucibus. Pellentesque posuere auctor pretium."]]))

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
               :font-size "1.1em"
               :justify-content "center"}}
      [:div#headline.headfont
        {:style {:font-size "6em"
                 :text-align "center"}}
        "Diaper Time"]]

    [description]

    [:div ;; flexbox
      {:style {:display "flex"
               :justify-content "center"
               :align-items "center"
               :margin "20px 5%"}}

      [test-drive-box]
      [or-box]
      [login-box]]])
