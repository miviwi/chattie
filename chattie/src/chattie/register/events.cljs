(ns chattie.register.events
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :as rf]

            [chattie.http.ajax :as ajax]))

(s/def ::user-data (s/keys :req-un [::email
                                    ::username
                                    ::display-name
                                    ::password]))

(rf/reg-event-fx
  ::register
  (fn [cofx [_ user-data]]
    {:pre [(s/valid? ::user-data user-data)]}

    {:register-xhrio user-data}))

(rf/reg-fx
  :register-xhrio
  (fn [user-data]
    (ajax/POST
      :url "http://localhost:3000/register"
      :body user-data
      :then #(rf/dispatch [:register-success %])
      :catch #(rf/dispatch [:register-error %]))))
