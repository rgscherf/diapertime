(ns templates.signup
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]
            [secretary.core :as secretary]))

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
            "Baby's first name must be (0 < X < 20) characters."
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
            "Passwords must match and be > 6 characters"
            nil)]
    (swap! user assoc :errors {:email-error email-error
                               :name-error name-error
                               :password-error password-error
                               :any (some identity [email-error name-error password-error])}
                      input-field input-value)))

(defn- error-row
  "Render a row to the input table with validation errors for a given field"
  [user error-field]
  (if-let [err-message (get-in @user [:errors error-field])]
    [:td
      err-message]))

(defn- make-input
  "Render an input field"
  [data-source id-and-binding-name input-type data-field]
  (let [validate-fn
          #(validate-assoc data-source data-field (-> % .-target .-value))]
    [:input {:id id-and-binding-name
             :name id-and-binding-name
             :type input-type
             :value (data-field @data-source)
             :on-change validate-fn}]))

(defn- make-label-and-input
  "Render a label, then call the input field and error field functions"
  [label data-source id-and-binding-name input-type data-field error-field]
  [:tr
    [:td
      [:label label]]
    [:td
      [make-input data-source id-and-binding-name input-type data-field]]
    (if error-field
      [error-row data-source error-field])])


(defn signup-page []
  (let [user-atom (atom {:email ""
                         :name ""
                         :password ""
                         :password-again ""})
        _ (validate-assoc user-atom :email "")
        is-user-valid? #(not (seq (get-in @user-atom [:errors :any])))]

    (fn []
      [:div
        [:form
          [:table
            [:tbody
              [make-label-and-input "email address" user-atom "user-name" "text" :email :email-error]
              [make-label-and-input "baby's first name" user-atom "baby-name" "text" :name :name-error]
              [make-label-and-input "Account password" user-atom "password" "password" :password nil]
              [make-label-and-input "Account password again" user-atom "password-again" "password" :password-again :password-error]]]
          [:button {:on-click
                      #(do
                          (.preventDefault %)
                          (if (is-user-valid?)
                            (ajax/GET "/newuser"
                              {:params {:email (:email @user-atom)
                                        :name (:name @user-atom)
                                        :password (:password @user-atom)}
                               :format :json
                               :handler (fn [body]
                                          (secretary/dispatch! "/login"))})))
                    :disabled (not (is-user-valid?))}
            "Continue"]]
        [:div
          "We will never, EVER sell or misuse your email address. We promise to contact you only when you change settings on your account."]])))
