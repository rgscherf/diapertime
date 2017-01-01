(ns clj-diaper.auth
  (:require [digest]
            [clojure.string :refer [join]]))

;; TEMP USER DB
(def user-store (atom [{:email "gsmith@gmail.com"
                        :name "GrahmSmith"
                        :password-hash (digest/sha-256 "password")
                        :auth-token "QWURBEZBBKARIEXNKVBPTZYEC"}
                       {:email "sarabara@baby.me"
                        :name "sarabara"
                        :password-hash (digest/sha-256 "sarabara")
                        :auth-token "RJDUBWNNYRSQNCMJGPPVDHKBN"}]))

;; GENERATE AUTH TOKEN
(defn- random-string
  []
  (join
    (take 25
      (repeatedly #(rand-nth "ABCDEFGHIJKLMNOPQRSTUVWXYZ")))))

(defn register-new-user
  [{:keys [email name password]}]
  (let [auth-token (random-string)
        new-user {:email email
                  :name name
                  :password-hash (digest/sha-256 password)
                  :auth-token auth-token}]
    (do
      (swap! user-store conj new-user)
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body "ok"
       :cookies {"auth-token" {:value auth-token
                               :max-age (* 60 60 24 120)}}})))

;; LOGIN -> AUTH
(defn- get-user-by-token
  [store token]
  (let [user (first (filter #(= (:auth-token %)
                                token)
                            @store))]
    (dissoc user :password-hash)))

(defn try-auth-token
  [{:keys [auth-token]}]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (if (nil? (get-user-by-token user-store auth-token))
             "error"
             "ok")})

;; LOGIN -> LOGIN
(defn- valid-password?
  [user-map password-input]
  (= (digest/sha-256 password-input)
     (:password-hash user-map)))

(defn- get-user-by-login
  [store email password]
  (let [user (first (filter #(= (:email %)
                                email)
                            @store))]
    (if (valid-password? user password)
      (dissoc user :password-hash))))

(defn try-password
  [params]
  (let [{:keys [email password]} params
        user (get-user-by-login user-store email password)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (if user
               (:auth-token user)
               "error")}))
