(ns clj-diaper.models.user
  (:require [monger.collection :as mcoll]
            [clj-diaper.db :as db]
            [digest]
            [clojure.string :refer [join]]))

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
; (get-user-by-token "FBECUTAFQOJFEVBIYZIQRSFZJ")

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
  [params]
  (let [{:keys [email password]} params
        user (get-user-by-login email password)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (if user
               (:auth-token user)
               "error")}))
