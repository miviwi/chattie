(ns chattie.login
  (:require [reagent.core :as r]
            [re-frame.core :as rf]

            ["@material-ui/core" :as mui]

            [chattie.user :as user]
            [chattie.login.events :as login-events]
            [chattie.routing :as routing]))

(defonce state (r/atom {:email-input ""
                        :password-input ""}))

(defn ev-target-val [ev]
  (-> ev .-target .-value))

(defn login-component []
  (fn []
    (let [user-identity @(rf/subscribe [:user-identity])]
      [:> mui/Container
       [:> mui/Card
        [:> mui/CardContent {:style {:display "flex"
                                     :flex-direction "column"}}

         [:> mui/TextField {:variant "standard" :color "primary"
                            :value (:email-input @state)
                            :on-change #(r/rswap! state assoc :email-input (ev-target-val %))
                            :label "Email"}]
         [:> mui/TextField {:type "password" :variant "standard" :color "primary"
                            :value (:password-input @state)
                            :on-change #(r/rswap! state assoc :password-input (ev-target-val %))
                            :label "Password"}]]

        [:> mui/CardActions {:style {:display "flex"
                                     :justify-content "flex-end"
                                     :margin-top "0.5rem"}}

         [:> mui/Button {:variant "contained" :color "primary"
                         :disabled (let [{:keys [email-input password-input]} @state]
                                     (some clojure.string/blank? [email-input password-input]))
                         :on-click #(let [{email :email-input password :password-input} @state]
                                      (rf/dispatch [::login-events/login {:email email :password password}]))}
          "Login"]]]
       (if-not (nil? user-identity)
         [:p (str "identity: " (-> user-identity clj->js JSON.stringify))])])))

(defmethod routing/router-outlet ::routing/login []
  [login-component])
