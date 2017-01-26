(ns templates.login
  (:require [reagent.cookies :as cookies]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]
            [secretary.core :as secretary :include-macros true]
            [templates.page-header :as header]
            [templates.form-styles :as fs]
            [accountant.core :as accountant]))

(defn- try-token-redirect
  [token]
  (if token
    (ajax/GET "/tryauthtoken"
      {:params {:auth-token token}
       :format :json
       :handler (fn [body]
                  (if (= "ok" body)
                    (accountant/navigate! "/events")))})))


(defn login-page []
  (js/scroll 0 0)
  (let [login-atom (atom {:email ""
                          :password ""
                          :error false})
        auth (cookies/get "auth-token")
        _ (try-token-redirect auth)]
    (fn []
      [:div
        [header/base-header]
        [:div
          (assoc-in fs/form-container-flex-style
                    [:style :margin-top]
                    "60px")
          [:div
            fs/form-field-style
            [:input (merge {:id "email"
                            :name "email"
                            :type "text"
                            :value (:email @login-atom)
                            :on-change #(swap! login-atom assoc :email (-> % .-target .-value))}
                           fs/form-text-input-style)]
            [:div
              [:label
                {:style {:font-size "1.4em"}}
                "Email address"]]]
          [:div
            fs/form-field-style
            [:input (merge {:id "password"
                            :name "password"
                            :type "password"
                            :value (:password @login-atom)
                            :on-change #(swap! login-atom assoc :password (-> % .-target .-value))}
                           fs/form-text-input-style)]
            [:div
              [:label
                {:style {:font-size "1.4em"}}
                "Password"]]]
          [:div
            fs/form-submit-buttons-style
            [:button
              (merge {:on-click #(accountant/navigate! "/")}
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
              {:style {:width "349px"
                       :display "flex"
                       :justify-content "flex-end"
                       :margin-top "20px"
                       :color "#FFA8DF"}}
              "Username/password not found!"])]])))
              
