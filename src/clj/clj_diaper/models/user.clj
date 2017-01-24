(ns clj-diaper.models.user
  (:require [monger.collection :as mcoll]
            [clj-diaper.db :as db]
            [digest]
            [clojure.string :refer [join]]
            [ring.util.response :as response]))

;; GENERATE AUTH TOKEN
(defn- random-string
  []
  (join
    (take 25
      (repeatedly #(rand-nth "ABCDEFGHIJKLMNOPQRSTUVWXYZ")))))

(defn insert-new-baby!
  [user]
  (mcoll/insert db/database
                db/babies
                user))

(defn register-new-user
  [{:keys [email name password]}]
  (let [auth-token (random-string)
        new-user {:email email
                  :baby-name name
                  :password-hash (digest/sha-256 password)
                  :auth-token auth-token
                  :events []}]
    (do
      (insert-new-baby! new-user)
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body "ok"
       :cookies {"auth-token" {:value auth-token
                               :max-age (* 60 60 24 120)}}})))

;; LOGIN -> AUTH
(defn get-user-by-token
  [token]
  (mcoll/find-one-as-map db/database
                         db/babies
                         {:auth-token token}))

(defn try-auth-token
  [{:keys [auth-token]}]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (if (nil? (get-user-by-token auth-token))
             "error"
             "ok")})

;; LOGIN -> LOGIN
(defn- valid-password?
  [user-map password-input]
  (= (digest/sha-256 password-input)
     (:password-hash user-map)))

(defn- get-user-by-login
  [email password]
  (if-let [user (mcoll/find-one-as-map db/database
                                       db/babies
                                       {:email email})]
    (if (valid-password? user password)
      (dissoc user :password-hash))))

(defn try-password
  [{:keys [email password]}]
  (let [user (get-user-by-login email password)]
    (response/response
      (if user
          (:auth-token user)
          "error"))))

(comment
  ; "new user --
  ; email rgscherf@gmail.com
  ; password hildabeast"
  (try-password {:email "hello@world.net"
                 :password "heraldofwoe"})
  (get-user-by-login "hello@world.net" "heraldofwoe")
  (mcoll/find-maps db/database db/babies)
  (digest/sha-256 "hildabeast")
  (= "d5fd9abf23962e5f0c45d24b388de56f73c00fa5d52b1400f2411dc21377357a"
     (digest/sha-256 "heraldofwoe"))
 (mcoll/find-one-as-map db/database
                       db/babies
                       {:email "hello@world.net"}))
