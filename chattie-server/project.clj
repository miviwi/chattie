(defproject chattie-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clj-time "0.15.2"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-cors "0.1.13"]
                 [ring/ring-json "0.5.0"]
                 [buddy "2.0.0"]
                 [com.novemberain/monger "3.1.0"]]

  :plugins [[lein-ring "0.12.5"]]

  :ring {:handler chattie-server.handler/app}

  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-devel "1.7.1"]
                        [ring/ring-mock "0.3.2"]]
         :source-paths ["src"]}})
