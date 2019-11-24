(ns chattie-server.db.users
  (:require [clojure.spec.alpha :as s]
            [monger.collection :as mc]
            [monger.operators :refer :all]

            [chattie-server.db.core :refer [db]])
  (:import [org.bson.types ObjectId]))

(def ^:private coll-users "users")

(s/def ::credentials (s/keys
                       :req-un [::email ::password]))

(s/def ::user-id #(instance? ObjectId %))
(s/def ::opt-user-id (s/nilable #(s/valid? ::user-id %)))

(s/def ::user-data (s/keys
                     :req-un [::username
                              ::username-stub
                              ::display-name
                              ::email
                              ::enc-password]
                     :opt-un [::active-token-id]))

(s/def ::new-user-data (s/keys
                         :req-un [::username
                                  ::display-name
                                  ::email
                                  ::enc-password]))

(defn query-user-id [{:keys [email] :as credentials}]
  {:pre [(s/valid? ::credentials credentials)]}

  (let [user-data (mc/find-one-as-map db coll-users {:email email} [] true)
        user-id (:_id user-data)]
    user-id))

(defn query-user-data [user-id]
  {:pre [(s/valid? ::user-id user-id)]}

  (let [user-data (mc/find-one-as-map db coll-users {:_id user-id} [] true)]
    user-data))

(defn uniquify-username [^String username]
  (let [num-username-stubs (mc/count db coll-users {:username-stub username})]
    (str username "#" (inc num-username-stubs))))

(defn create-user! [user-data]
  {:pre [(s/valid? ::new-user-data user-data)]
   :post [(s/valid? ::opt-user-id %)]}

  (let [{:keys [username display-name email enc-password]} user-data
        user-exists (mc/any? db coll-users {:email email})]
    (if user-exists
      nil                  ;; A user with the given email address alreay exists
      
      (let [uniq-username (uniquify-username username)
            user-data {:username uniq-username
                       :username-stub username
                       :display-name display-name
                       :email email
                       :enc-password enc-password}]
        (s/valid? ::user-data user-data)

        ;; Insert the new user into the database and return their id
        (-> (mc/insert-and-return db coll-users user-data) :_id)))))

(defn store-active-token-id! [& {:keys [user-id token-id] :as options}]
  {:pre [(s/valid? ::user-id user-id)]}

  (mc/update db coll-users {:_id user-id} {$set {:active-token-id token-id}}))

(defn user-id-str [user-id]
  {:pre [(s/valid? ::user-id user-id)]}

  (.toHexString user-id))
