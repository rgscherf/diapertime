
(ns templates.landing-page)

(defn- action-box-style
  [width]
  {:style {:width (str width "%")
           :display "flex"
           :justify-content "center"
           :align-items "center"
           :font-size "2em"}})

(defn- modify-style
  [m kv]
  (update-in m [:style] merge kv))

(defn- auth-box
  [type]
  (let [is-login (= type :login)
        glyph (str "fa fa-" (if is-login "sign-in" "user-plus"))]
    [:div
      (action-box-style 40)
      [:a.landingLink {:href (if is-login "/login" "/signup")}
        [:i {:class glyph :aria-hidden "true"
             :style {:margin-right "10px" :color "#FFA8DF"}}]
        [:span
            (if is-login "Log in" "Sign up")]]]))

(defn- signup-box
  []
  [auth-box :signup])

(defn login-box
  []
  [auth-box :login])

(defn- auth-boxes
  []
  [:div
    {:style {:display "flex"
             :justify-content "space-around"
             :align-items "center"
             :margin "60px 5%"}}
    [login-box]
    [signup-box]])

(defn- description
  []
  (let [subhead-style
          {:style {:margin-top "60px"
                   :font-size "2em"
                   :text-align "center"}}
        body-style
          {:style {:margin-top "60px"
                   :font-size "1.1em"
                   :text-align "justify"
                   :line-height "1.5"
                   :max-width "70%"
                   :margin-left "auto"
                   :margin-right "auto"}}]

    [:div
      [:div
        subhead-style
        "Forget complicated baby tracking apps."]
      [:div
        body-style
        [:p "Diaper Time represents each period of your baby's wakefulness as one event.
              Enter the important information for that event
              and instantly see how it compares to historical data."]
        [:p "We built Diaper Time as the quickest, easiest way to track our babies' I/O."]
        [:p "Sound interesting? "
          [:a {:href "/random"} "Go for a test drive"]
          [:span " with some randomly generated data."]]]]))

(defn render-landing-page
  []
  [:div
    {:style {:max-width "800px"
             :margin "0px auto"}}
    [:div#headline.headfont
      {:style {:font-size "6.5em"
               :margin-top "40px"
               :text-align "center"}}
      "Diaper Time"]
    [description]
    [auth-boxes]])
