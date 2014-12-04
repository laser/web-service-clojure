(defproject tutorial "0.1.0-SNAPSHOT"
  :dependencies [[com.h2database/h2 "1.3.170"]
                 [compojure "1.2.2"]
                 [oj "0.2.1" :exclusions [commons-codec]]
                 [org.clojure/clojure "1.6.0"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1"]]
  :description "A simple web service, in Clojure"
  :joplin {:migrators {:sql-mig "db/migrations"}
           :seeds {:sql-seed "seeds.sql/run"}
           :databases {:sql-dev {:type :sql, :url "jdbc:h2:file:db/tutorial-dev"}}
           :environments {:dev [{:db :sql-dev, :migrator :sql-mig, :seed :sql-seed}]}}
  :license {:name "MIT" :url "http://opensource.org/licenses/MIT"}
  :main tutorial.handler
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.13" :exclusions [org.clojure/clojure]]
            [joplin.lein "0.2.2" :exclusions [org.clojure/clojure]]]
  :profiles {:production {:env {:production true}}
             :dev {:dependencies [[org.clojure/java.jdbc "0.3.6"]]}}
  :ring {:handler tutorial.handler/app}
  :source-paths ["src" "db"]                            ;;;;; must add this or the seeds won't be found
  :uberjar-name "tutorial-standalone.jar"
  )
