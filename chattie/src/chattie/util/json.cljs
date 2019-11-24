(ns chattie.util.json)

(defn pretty-str [obj]
  (println "(object? obj)" (object? obj) "(array? obj)" (array? obj))
  (let [json-object (cond
                      (or (object? obj) (array? obj)) obj
                      :else (clj->js obj))]
    (js/JSON.stringify json-object nil 4)))

;(defmethod pretty-str :js [obj]
;  (js/JSON.stringify obj nil 4))
;(defmethod pretty-str :clj [obj]
;  (js/JSON.stringify (clj->js obj) nil 4))

