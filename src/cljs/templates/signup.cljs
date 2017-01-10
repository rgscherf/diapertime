(ns templates.signup
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]
            [secretary.core :as secretary]
            [templates.page-header :as header]
            [templates.form-styles :as fs]
            [accountant.core :as accountant]))

(defn- validate-assoc
  "Validate user atom input, then assoc new values into atom."
  [user input-field input-value]
  (let [nil-error {:is false :error ""}
        email-error
          (if (not (re-matches #".+@.+\..+" (:email @user)))
            "Must be a valid email address."
            nil)
        name-error
          (if (not (< 1 (count (:name @user)) 20))
            "Must be (0 < x < 20) characters."
            nil)
        password-error
        ;; these validation conditions are checked based on the
        ;; previous state of the input atom.
        ;; so if you match passwords on keystroke [0], we won't validate
        ;; that input until keystroke [1]
        ;; solution: check the incoming input before it's swapped
        ;; into the input atom.
          (if (not (and (cond
                          (= input-field :password) (= input-value (:password-again @user))
                          (= input-field :password-again) (= (:password @user) input-value))
                        (< 5 (count (:password @user)))
                        (< 5 (count (:password-again @user)))))
            "Passwords must match and be > 6 characters."
            nil)]
    (swap! user assoc :errors {:email-error email-error
                               :name-error name-error
                               :password-error password-error
                               :any (some identity [email-error name-error password-error])}
                      input-field input-value)))

(defn- make-input
  "Render an input field"
  [data-source id-and-binding-name input-type data-field]
  (let [validate-fn
          #(validate-assoc data-source data-field (-> % .-target .-value))]
    [:input
      (merge {:id id-and-binding-name
              :name id-and-binding-name
              :type input-type
              :value (data-field @data-source)
              :on-change validate-fn}
             fs/form-text-input-style)]))

(defn- make-label-and-input
  "Render a label, then call the input field and error field functions"
  [label desc data-source id-and-binding-name input-type data-field error-field]
  (let [small-style {:style {:opacity "0.7"
                             :font-size "0.95em"}}]
    [:div
      fs/form-field-style
      [make-input data-source id-and-binding-name input-type data-field]
      [:div
        [:span
          {:style {:font-size "1.4em"}}
          label]
        (if desc
          [:span
            small-style
            (str " — " desc)])]
      (if-let [er (get-in @data-source [:errors error-field])]
        [:div
          small-style
          er])]))


(defn signup-page []
  (js/scroll 0 0)
  (let [user-atom (atom {:email ""
                         :name ""
                         :password ""
                         :password-again ""})
        _ (validate-assoc user-atom :email "")
        is-user-valid? #(not (seq (get-in @user-atom [:errors :any])))]
    (fn []
      [:div
        [header/base-header]
        [:div
          fs/form-container-flex-style
          [make-label-and-input "Email address"
                                "You'll log in with this."
                                user-atom
                                "user-name"
                                "text"
                                :email
                                :email-error]
          [make-label-and-input "Baby's name"
                                "We keep this private."
                                user-atom
                                "baby-name"
                                "text"
                                :name
                                :name-error]
          [make-label-and-input "Account password"
                                nil
                                user-atom
                                "password"
                                "password"
                                :password
                                nil]
          [make-label-and-input "Account password again"
                                nil
                                user-atom
                                "password-again"
                                "password"
                                :password-again
                                :password-error]
          [:div
            fs/form-submit-buttons-style
            [:button
              (merge {:on-click #(accountant/navigate! "/")}
                     fs/form-button-style)

              "Back"]
            [:button
              (merge
                {:on-click
                  #(do
                      (.preventDefault %)
                      (if (is-user-valid?)
                        (ajax/GET "/newuser"
                          {:params {:email (:email @user-atom)
                                    :name (:name @user-atom)
                                    :password (:password @user-atom)}
                           :format :json
                           :handler (fn [body]
                                      (accountant/navigate! "/login"))})))
                  :disabled (not (is-user-valid?))}
                fs/form-button-style)
              "Go!"]]]])))
