(defproject allocations "0.1.0-SNAPSHOT"
  :dependencies [[com.h2database/h2 "1.3.170"]
                 [compojure "1.2.0"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1"]]
  :description "C5 Resource Allocations"
  :joplin {
           :migrators {:sql-mig "db/migrations"}
           :seeds {:sql-seed "seeds.sql/run"}
           :databases {:sql-dev {:type :sql, :url "jdbc:h2:file:db/allocations-dev"} }
           :environments {:dev [{:db :sql-dev, :migrator :sql-mig, :seed :sql-seed}] }
           }
  :license {:name "MIT" :url "http://opensource.org/licenses/MIT"}
  :main allocations.handler
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.13"] [joplin.lein "0.2.2"]]
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"] [ring-mock "0.1.5"]]}
             :production {:env {:production true}}}
  :ring {:handler allocations.handler/app}
  :source-paths ["src" "db"]                            ;;;;; must add this or the seeds won't be found
  :uberjar-name "allocations-standalone.jar"
  :url "http://www.carbonfive.com"
  )
