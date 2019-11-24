(ns chattie.auth
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :as rf]))

(s/def ::email string?)
(s/def ::password string?)
(s/def ::login-credentials (s/keys :req-un [::email ::password]))

(s/def ::token string?)

(s/def ::identity (s/keys :req [::token]))
