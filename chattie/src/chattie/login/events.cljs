(ns chattie.login.events
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :as rf]
            
            [chattie.http.ajax :as ajax]

            [chattie.db :as db]
            [chattie.auth :as auth]
            [chattie.user :as user]))

(rf/reg-event-fx
  ::login
  (fn [cofx [_ credentials]]
    {:pre [(s/valid? ::auth/login-credentials credentials)]}

    {:login-xhrio credentials}))

(rf/reg-fx
  :login-xhrio
  (fn [{:keys [email password] :as credentials}]
    (ajax/POST
      :url "http://localhost:3000/login"
      :body credentials
      :then #(do (println "POST /login" credentials "=>" (JSON.stringify (clj->js %) nil 4))
                 (rf/dispatch [:store-login-data %])))))
