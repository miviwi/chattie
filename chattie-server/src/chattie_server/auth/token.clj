(ns chattie-server.auth.token
  (:require [clojure.spec.alpha :as s]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [buddy.core.nonce :as nonce]
            [buddy.core.codecs :refer [bytes->hex hex->bytes]]
            [buddy.sign.jwt :as jwt]
            
            [chattie-server.util.server :as server]
            [chattie-server.db.users :as users])
  
  (:import [org.bson.types ObjectId]))

(s/def ::jwt-token string?)
(s/def ::token-id (s/and
                    string?

                    ;; The id is a 24-byte hex-string (so every 2 charaters represent 1 byte)
                    #(re-matches #"(?i)[0-9a-f]{48}" %)))

(def ^:private secret-key "secret-key")

(defn ^:private to-jwt-time [datetime]
  (-> datetime tc/to-epoch (* 1000)))

(def ^:private token-validity-duration
  (t/minutes 15))

(defn gen-token [user-id]
  {:pre [(s/valid? ::users/user-id user-id)]
   :post [(s/valid? (s/keys :req-un [::token ::id]) %)]}

  (let [server-name (server/get-my-hostname)

        now (t/now)
        iat (to-jwt-time now)
        exp (-> now (t/plus token-validity-duration) to-jwt-time)
        id (bytes->hex (nonce/random-bytes 16))

        claims {:iss (str server-name "/chattie-server")
                :aud (users/user-id-str user-id)
                :iat iat
                :exp exp
                :id id}
        token (jwt/sign claims secret-key)]
    {:token token :id id}))

(defn ^:private get-token-data [token]
  {:pre [(s/valid? ::jwt-token token)]}

  (let [now (to-jwt-time (t/now))]
    (try
      (jwt/unsign token secret-key {:now now})

      (catch Exception _ 
        nil))))

(defn token-id
  "Returns the unique 16-byte id attached to each token
    encoded as a hex-string or 'nil' if the token has
    expired"

  [token]

  {:pre [(s/valid? ::jwt-token token)]
   :post [(s/valid? ::token-id %)]}

  (let [token-data (get-token-data token)]
    (if-not (nil? token-data)
      (:id token-data)
      nil)))

(defn get-token-id-user-id
  "Returns the :chattie-server.db.users/user-id and unique per-token id
    encoded in the JWT if it's still valid or 'nil' otherwise (i.e. if
    it has expired)
  
  {:user-id ^chattie-server.db.users/user-id :token-id ^chattie-server.auth.token/token-id}"

  [token]

  {:pre [(s/valid? ::jwt-token token)]
   :post [(s/valid? (s/nilable (s/keys ::req-un [::user-id ::token-id])))]}

  (let [token-data (get-token-data token)]
    (if-not (nil? token-data)
      (-> token-data :aud ObjectId.)
      nil)))
