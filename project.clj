(defproject allocations "0.1.0-SNAPSHOT"
  :description "C5 Resource Allocations"
  :url "http://www.carbonfive.com"
  :license {:name "MIT" :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.16"]]
  :main ^:skip-aot allocations.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
