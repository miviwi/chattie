(ns chattie-server.db.core
  (:require [monger.core :as mg]
            [monger.db]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object to-db-object]])
  
  (:import [com.mongodb DBObject]
           [org.bson.types ObjectId]))

(def ^:private database-uri "mongodb://localhost:27017/chattie")
(def ^:private database-connection (mg/connect-via-uri database-uri))

(def db (:db database-connection))

(println "connected to database" database-uri)
(println "\tcollections:" (monger.db/get-collection-names db))
