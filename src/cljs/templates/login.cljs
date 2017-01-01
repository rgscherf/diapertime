(ns templates.login
  (:require [reagent.cookies :as cookies]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]
            [secretary.core :as secretary :include-macros true]))

(defn- try-token-redirect
  [token]
  (if token
    (ajax/GET "/tryauthtoken"
      {:params {:auth-token token}
       :format :json
       :handler (fn [body]
                  (if (= "ok" body)
                    (secretary/dispatch! "/events")))})))

(defn login-page []
  (let [login-atom (atom {:email ""
                          :password ""
                          :error false})
        auth (cookies/get "auth-token")
        _ (try-token-redirect auth)]
    (fn []
      [:div
        [:div
          [:label "Email address"]
          [:input {:id "email"
                   :name "email"
                   :type "text"
                   :value (:email @login-atom)
                   :on-change #(swap! login-atom assoc :email (-> % .-target .-value))}]]
        [:div
          [:label "Password"]
          [:input {:id "password"
                   :name "password"
                   :type "text"
                   :value (:password @login-atom)
                   :on-change #(swap! login-atom assoc :password (-> % .-target .-value))}]]
        [:div
          [:button {:on-click
                      #(do
                          (.preventDefault %)
                          (ajax/GET "/trypassword"
                            {:params {:email (:email @login-atom)
                                      :password (:password @login-atom)}
                             :format :json
                             :handler (fn [body]
                                        (if (= "error" body)
                                          (swap! login-atom assoc :error true)
                                          (do
                                            (swap! login-atom assoc :error false)
                                            (cookies/set! "auth-token" body {:max-age (* 60 60 24 120)})
                                            (secretary/dispatch! "/events"))))}))
                    :disabled (or (empty? (:email @login-atom))
                                  (empty? (:password @login-atom)))}
              "Login!"]]
        (if (:error @login-atom)
          [:div
            "Username/password not found!"])])))
