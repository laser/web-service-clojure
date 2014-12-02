(defproject allocations "0.1.0-SNAPSHOT"
  :dependencies [[com.h2database/h2 "1.3.170"]
                 [compojure "1.2.0"]
                 [hiccup "1.0.2"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.3.2"]]
  :description "C5 Resource Allocations"
  :license {:name "MIT" :url "http://opensource.org/licenses/MIT"}
  :main allocations.handler
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.13"]]
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"] [ring-mock "0.1.5"]]}}
  :ring {:handler allocations.handler/app}
  :url "http://www.carbonfive.com"
)
