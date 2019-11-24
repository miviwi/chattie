(ns chattie.app
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bidi.bidi :as bidi]

            ["@material-ui/core" :as mui]
            
            [chattie.routing :as routing]
            [chattie.login :as login]))

(defn app-component []
  (fn []
    [:> mui/Container
     [:> mui/Typography {:variant "h2"
                         :style {:margin "1rem 0"}}
      "Chattie"]

     [:a {:href (routing/path-for ::routing/login)}
      [:> mui/Typography {:variant "subtitle1"} "Login"]]
     
     [:a {:href (routing/path-for ::routing/register)}
      [:> mui/Typography {:variant "subtitle1"} "Register"]]]))

(defmethod routing/router-outlet ::routing/index []
  [app-component])
