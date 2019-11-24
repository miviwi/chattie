(ns chattie.db
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :as rf]))

(s/def ::app-db
  (s/keys :opt [:chattie.user/user]))

(defn app-db-valid [db]
  (s/valid? ::app-db db))

(rf/reg-event-db
  ::init-db
  (fn [_ _]
    {:post [(app-db-valid %)]}

    (let [db {}]
      db)))

(rf/reg-cofx
  :session-storage
  (fn [cofx ^String key]
    (let [item (js->clj (js/sessionStorage.getItem key))]
      (assoc cofx :session-storage item))))

(rf/reg-fx
  :session-storage-store
  (fn [{:keys [key value]}]
    {:pre [(string? key)
           (string? value)]}

    (js/sessionStorage.setItem key value)))
