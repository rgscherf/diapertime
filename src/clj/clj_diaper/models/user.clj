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

(defn insert-user-map!
  [user]
  (mcoll/insert db/database
                db/user-collection
                user))

(defn insert-new-baby!
  [user]
  (mcoll/insert db/database
                db/baby-collection
                {:name (:baby-name user)
                 :events []}))

(defn register-new-user
  [{:keys [email name password]}]
  (let [auth-token (random-string)
        new-user {:email email
                  :baby-name name
                  :password-hash (digest/sha-256 password)
                  :auth-token auth-token}]
    (do
      (insert-user-map! new-user)
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
                         db/user-collection
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
                                       db/user-collection
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
