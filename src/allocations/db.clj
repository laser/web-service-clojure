(ns allocations.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname "db/allocations"})

(defn create-location
  [x y]
  (let [results (sql/with-connection db-spec
                  (sql/insert-record :locations
                                     {:x x :y y}))]
    (assert (= (count results) 1))
    (first (vals results))))

(defn read-location
  [loc-id]
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select x, y from locations where id = ?" loc-id]
                    (doall res)))]
    (assert (= (count results) 1))
    (first results)))

(defn read-locations
  []
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select id, x, y from locations"]
                    (doall res)))]
    results))
