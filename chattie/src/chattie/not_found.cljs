(ns chattie.not-found
  (:require [reagent.core :as r]
            [bidi.bidi :as bidi]

            [chattie.routing :as routing]))

(defmethod routing/router-outlet ::routing/not-found []
  (fn []
    [:div
     [:h1 "Page not found."]]))
