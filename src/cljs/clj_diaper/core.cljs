(ns clj-diaper.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]

              [clj-diaper.mainpage :as mainpage]
              [templates.login :as login]
              [templates.signup :as signup]
              [templates.landing-page :as landing-page]))

;; VIEW FUNCTIONS

(defn view-landing-page []
  (landing-page/render-landing-page))

(defn view-diaper-events []
  (mainpage/main-page-container false))

(defn view-random-events []
  (mainpage/main-page-container true))

(defn login-page []
  (login/login-page))

(defn signup-page []
  (signup/signup-page))

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes


(secretary/defroute "/" []
  (session/put! :current-page #'view-landing-page))

(secretary/defroute "/events" []
  (session/put! :current-page #'view-diaper-events))

(secretary/defroute "/random" []
  (session/put! :current-page #'view-random-events))

(secretary/defroute "/login" []
  (session/put! :current-page #'login-page))

(secretary/defroute "/signup" []
  (session/put! :current-page #'signup-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
