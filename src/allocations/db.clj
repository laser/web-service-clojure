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

    (if (= 1 (count results))
      {:status :success :result (->> results vals first (assoc {:text text} :id))}
      {:status :failure :message (format "Error: Could not create todo with text %s" text)})))

(defn read-todo
  [id]
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select id, text from todos where id = ?" id]
                    (doall res)))]

    (if (= 1 (count results))
      {:status :success :result (first results)}
      {:status :failure :message (format "Error: No todo found with id %s" id)})))

(defn read-todos
  []
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select id, text from todos"]
                    (doall res)))]
    results))

(defn delete-todo
  [id]
  (let [deleted (sql/with-connection db-spec
                  (sql/delete-rows :todos ["id = ?" id]))]
    (if (= 1 (first deleted))
      {:status :success :result nil}
      {:status :failure :message (format "Error: Could not delete todo with id %d" id)})))

(defn update-todo
  [id text]
  (let [updated (sql/with-connection db-spec
                  (sql/update-values :todos ["id = ?" id] {:text text}))]
    (if (= 1 (first updated))
      {:status :success :result {:text text :id id}}
      {:status :failure :message (format "Error: Could not update todo with id %d" id)})))
