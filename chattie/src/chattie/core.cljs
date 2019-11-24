(ns chattie.core
    (:require [reagent.core :as r]
              [reagent.session :as session]
              [re-frame.core :as rf]

              ["@material-ui/core" :as mui]

              [chattie.db :as db]    ;; Make sure it doesn't get optimized out
              
              [chattie.routing :as routing]

              ;; List all of the components so they are
              ;;   included in the compiled app
              [chattie.app]
              [chattie.login]
              [chattie.register]
              [chattie.not-found]
              
              [chattie.http.ajax :as ajax]))
              
(enable-console-print!)

(defn current-page []
  (fn []
    (let [route (session/get :route)
          current-page (:current-page route)]
      [:> mui/MuiThemeProvider
       ^{:key current-page} [routing/router-outlet current-page]])))

(defn render []
  (r/render-component [current-page]
                      (js/document.getElementById "app")))

(defn bootstrap []
  (rf/dispatch-sync [::db/init-db])
  (routing/bootstrap-navigation!)
  (render))

(defn on-js-reload []
  (rf/clear-subscription-cache!)
  (bootstrap))

(bootstrap)

;(ajax/POST :url "http://localhost:3000/register"
;           :body {:username "brunon"
;                  :display-name "Brunon"
;                  :email "brunon@brunon.org"
;                  :password "asdf"}
;           :then #(println "POST /register =>" (JSON.stringify (clj->js %) nil 4)))
