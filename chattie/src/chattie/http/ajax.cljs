(ns chattie.http.ajax
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async :refer [<!]]
            [cljs-http.client :as http]
            
            [chattie.util.json :as json]))

(defn ^:private unhandled-error [{:keys [error-text body] :as response}]
  (throw (js/Error. (str "The server responded with a " error-text " => " (json/pretty-str body)))))

(defn POST [& {:keys [url body then catch], :or {then #() catch unhandled-error} :as options}]
  (go (let [response
            (<! (http/post url {:json-params body}))
            success (:success response)]
          (if success
            (then response)
            (catch response)))))

