(ns chattie-server.util.response
  (:require [chattie-server.util.status :refer [status]]))

(def ^:private _body-success {:status "success"})
(def ^:private _body-error {:status "error"})

(defn ^:private body-success [body]
  (merge body _body-success))
(defn ^:private body-error [body]
  (merge body _body-error))

(defn ok [response]
  {:status (status "OK")
   :body (body-success response)})

(defn created [response]
  {:status (status "Created")
   :body (body-success response)})

(defn unauthorized [response]
  {:status (status "Unauthorized")
   :body (body-error response)})

(defn not-found [response]
  {:status (status "Not Found")
   :body (body-error response)})

(defn conflict [response]
  {:status (status "Conflict")
   :body (body-error response)})
