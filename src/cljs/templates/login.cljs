(ns templates.login
  (:require [reagent.cookies :as cookies]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]
            [secretary.core :as secretary :include-macros true]
            [templates.page-header :as header]
            [templates.form-styles :as fs]))

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
        [header/base-header]
        [:div
          fs/form-container-flex-style
          [:div
            fs/form-field-style
            [:input (merge {:id "email"
                            :name "email"
                            :type "text"
                            :value (:email @login-atom)
                            :on-change #(swap! login-atom assoc :email (-> % .-target .-value))}
                           fs/form-text-input-style)]
            [:div
              [:label "Email address"]]]
          [:div
            fs/form-field-style
            [:input (merge {:id "password"
                            :name "password"
                            :type "password"
                            :value (:password @login-atom)
                            :on-change #(swap! login-atom assoc :password (-> % .-target .-value))}
                           fs/form-text-input-style)]
            [:div
              [:label "Password"]]]
          [:div
            {:style {:width "300px"
                     :display "flex"
                     :justify-content "flex-end"
                     :align-items "center"
                     :margin-top "20px"}}
            [:button
              (merge {:on-click #(secretary/dispatch! "/")}
                     fs/form-button-style)

              "Back"]
            [:button
              (merge {:on-click
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
                     fs/form-button-style)

              "Go!"]]
          (if (:error @login-atom)
            [:div
              {:style {:width "300px"
                       :display "flex"
                       :justify-content "flex-end"
                       :margin-top "20px"}}
              "Username/password not found!"])]])))
