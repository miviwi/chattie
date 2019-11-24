(ns chattie-server.auth.register
  (:require [buddy.hashers :as hs]
            
            [chattie-server.util.response :as response]
            [chattie-server.db.core :as db]
            [chattie-server.db.users :as users]))


(defn handler [params]
  (let [{:keys [username email password display-name]} params
        enc-password (hs/derive password)
        
        user-data {:username username
                   :display-name display-name
                   :email email
                   :enc-password enc-password}

        user-id (users/create-user! user-data)]

    (if-not (nil? user-id)
      (response/created {:message "registration successful" :id (users/user-id-str user-id)})
      (response/conflict {:message "a user with this e-mail has already been registered"}))))
    





