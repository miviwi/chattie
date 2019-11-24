(ns chattie.register
  (:require [reagent.core :as r]
            [re-frame.core :as rf]

            ["@material-ui/core" :as mui]

            [chattie.user :as user]
            [chattie.routing :as routing]))

(defonce state (r/atom {:email-input ""
                        :username-input ""
                        :display-name-input ""
                        :password-input1 ""
                        :password-input2 ""}))

(defn register-component []
  (fn []
    [:> mui/Container
     [:> mui/Card
      [:> mui/CardContent {:style {:display "flex"
                                   :flex-direction "column"}}
       [:> mui/Typography {:variant "h5"}
        "register-component"]]]]))

(defmethod routing/router-outlet ::routing/register []
  [register-component])

