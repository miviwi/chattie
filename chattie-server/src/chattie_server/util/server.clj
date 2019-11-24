(ns chattie-server.util.server
  (:import [java.net InetAddress]))

(defn get-my-hostname []
  (let [localhost (InetAddress/getLocalHost)]
    (.getHostName localhost)))
