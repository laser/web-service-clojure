(ns allocations.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname "db/allocations"})

(defn create-todo
  [text]
  (let [results (sql/with-connection db-spec
                  (sql/insert-record :todos
                                     {:text text}))]

    (-> results count (partial = 1) assert)
    (->> results vals first (assoc {:text text} :id))))

(defn read-todo
  [todo-id]
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select id, text from todos where id = ?" todo-id]
                    (doall res)))]

    (-> results count (partial = 1) assert)
    (first results)))

(defn read-todos
  []
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select id, text from todos"]
                    (doall res)))]
    results))
