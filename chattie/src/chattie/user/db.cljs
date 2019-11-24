(ns chattie.user
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :as rf]
            
            [chattie.db :as db]
            [chattie.auth :as auth]))

(s/def ::user (s/keys :req [::auth/identity]))

(rf/reg-sub
  :user-identity
  (fn [db _]
    (get-in db [::user ::auth/identity])))

(rf/reg-event-fx
  :store-login-data
  (fn [{db :db} [_ login-response]]
    {:post [(db/app-db-valid %)]}

    (let [token (get-in login-response [:body :token])]
      {:db (assoc db ::user {::auth/identity {::auth/token token}})

       :session-storage-store {:key "login-token" :value token}})))
