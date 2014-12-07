(defproject tutorial "0.1.0-SNAPSHOT"
  :dependencies [[com.h2database/h2 "1.3.170"]
                 [compojure "1.2.2"]
                 [environ "1.0.0"]
                 [oj "0.2.1" :exclusions [commons-codec]]
                 [org.clojure/clojure "1.6.0"]
                 [ragtime/ragtime.sql.files "0.3.8"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1"]]

  :description "A simple web service, in Clojure"

  :license {:name "MIT" :url "http://opensource.org/licenses/MIT"}

  :min-lein-version "2.0.0"

  :plugins [[ragtime/ragtime.lein "0.3.8"]
            [lein-ring "0.8.13" :exclusions [org.clojure/clojure]]
            [lein-environ "1.0.0"]]

  :profiles {
             :shared {:ragtime {:migrations ragtime.sql.files/migrations}}
             :production [:shared {:database ~(get (System/getenv) "DATABASE_URL")}]
             :dev [:shared {:env {:database-url "jdbc:h2:file:db/tutorial"}
                            :ragtime {:database "jdbc:h2:file:db/tutorial"}}]
             :test [:shared {:env {:database-url "jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1"}
                            :dependencies [[ring-mock "0.1.5"]
                                           [org.clojure/data.json "0.2.5"]]
                            :ragtime {:database "jdbc:h2:file:db/tutorial"}}]}

  :ring {:handler tutorial.handler/app}

  :source-paths ["src" "db"])
