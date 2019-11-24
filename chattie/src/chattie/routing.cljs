(ns chattie.routing
  (:require [reagent.core :as r]
            [reagent.session :as session]

            [bidi.bidi :as bidi]

            [accountant.core :as accountant]
            [clerk.core :as clerk]))

(def app-routes
  ["/" {"" ::index
        "login" ::login
        "register" ::register

        true ::not-found}])

(defn path-for [^keyword route]
  (bidi/path-for app-routes route))

(defmulti router-outlet identity)

(defn bootstrap-navigation! []
  (clerk/initialize!)
  (accountant/configure-navigation!
    {:nav-handler (fn [path]
                    (r/after-render clerk/after-render!)
                    (let [matched-route (bidi/match-route app-routes path)
                          current-page (:handler matched-route)
                          route-params (:route-params matched-route)]
                      (session/put! :route {:current-page current-page
                                            :route-params route-params}))
                    (clerk/navigate-page! path))
     :path-exists? (fn [path]
                     (boolean (bidi/match-route app-routes path)))})
  (accountant/dispatch-current!)

  (console.log "navigation bootstrap done"))


