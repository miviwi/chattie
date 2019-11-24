(ns chattie-server.auth.login
  (:require [buddy.hashers :as hs]

            [chattie-server.util.response :as response]
            [chattie-server.db.users :as users]
            [chattie-server.auth.token :as auth-token]))

(defn handler [params]
  (let [{:keys [email password]} params
        credentials {:email email :password password}
        user-id (users/query-user-id credentials)]

    (if-not (nil? user-id)
      (let [user-data (users/query-user-data user-id)
            password-matches? (hs/check password (:enc-password user-data))]
        (if password-matches?
          (let [{:keys [token id]} (auth-token/gen-token user-id)]
            (users/store-active-token-id! :user-id user-id :token-id id)
            (response/ok {:token token}))

          (response/unauthorized {:message "wrong email or password"}))))))
