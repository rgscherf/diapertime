(ns templates.landing-page
  (:require [accountant.core :as accountant]
            [templates.page-header :as header]))

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
      (action-box-style 50)
      [:a.landingLink {:href (if is-login "/login" "/signup")
                       :style {:display "flex"
                               :justify-content "center"
                               :align-items "center"
                               :flex-wrap "wrap"}}
        [:i {:class glyph :aria-hidden "true"
             :style {:margin-right "10px" :color "#FFA8DF"}}]
        [:span
            {:style {:width "120px"
                     :text-align "center"}}
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
        [:p "I built Diaper Time because nothing else provided a quick, elegant way to track my baby's I/O."]
        [:p "Sound interesting? "
          [:a
            {:href "/random"
             :on-click #(do (.preventDefault %) (accountant/navigate! "/random"))}
            "Go for a test drive"]
          [:span " with some randomly generated data."]]
        [:p
          [:a {:href "http://twitter.com/rgscherf"} "Get in touch"]
          [:span " to make feature suggestions!"]]]]))

(defn render-landing-page
  []
  (do
    (js/scroll 0 0))
  [:div
    {:style {:max-width "800px"
             :margin "0px auto"}}
    [header/base-header]
    [description]
    [auth-boxes]])
