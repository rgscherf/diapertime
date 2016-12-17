(ns clj-diaper.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [clj-diaper.mainpage :as content]))

;; -------------------------
;; Views

; (defn home-page []
;   [:div [:h2 "Welcome to clj-diaper's LANDING page"]
;    [:div [:a {:href "/about"} "go to about page"]]
;    [:div [:a {:href "/main"} "find out more...?"]]])

; (defn about-page []
;   [:div [:h2 "About clj-diaper's About page"]
;    [:div [:a {:href "/"} "go to the home page"]]])

(defn view-diaper-events []
  (content/main-page-container))

(defn view-random-events []
  (content/main-page-container))

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

; (secretary/defroute "/main" []
;   (session/put! :current-page #'main-page))

(secretary/defroute "/" []
  (session/put! :current-page #'view-diaper-events))

(secretary/defroute "/random" []
  (session/put! :current-page #'view-random-events))

; (secretary/defroute "/about" []
;   (session/put! :current-page #'about-page))

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
