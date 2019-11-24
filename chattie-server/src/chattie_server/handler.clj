(ns chattie-server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]

            [ring.util.request :as req]
            [ring.middleware.keyword-params :as ring-keyword-params]
            [ring.middleware.cors :as ring-cors]
            [ring.middleware.json :as ring-json]
            
            [chattie-server.util.response :as response]
            [chattie-server.auth.login :as login]
            [chattie-server.auth.register :as register]))

(defroutes app-routes
  (GET "/" [] {:body {:status "success"}})

  (POST "/login" {params :params} (login/handler params))
  (POST "/register" {params :params} (register/handler params))

  (route/not-found (response/not-found {:message "Not found"})))

(def app
  (->
    (ring-cors/wrap-cors app-routes
                         :access-control-allow-origin [#"http://localhost:3449"]
                         :access-control-allow-methods [:get :post :put :delete]
                         :access-control-allow-credentials ["true"])
     
    ring-keyword-params/wrap-keyword-params
    ring-json/wrap-json-params
    ring-json/wrap-json-response))
