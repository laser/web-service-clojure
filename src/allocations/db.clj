(ns allocations.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname "db/allocations-dev"})

(defn create-todo
  [text completed]
  (let [to-create {:text text :completed completed}
        results (sql/with-connection db-spec
                  (sql/insert-record :todos to-create))]

    (if (= 1 (count results))
      {:status :success :result (->> results vals first (assoc to-create :id))}
      {:status :failure :message (format "Error: Could not create todo with text %s" text)})))

(defn read-todo
  [id]
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select id, text, completed from todos where id = ?" id]
                    (doall res)))]

    (if (= 1 (count results))
      {:status :success :result (first results)}
      {:status :failure :message (format "Error: No todo found with id %s" id)})))

(defn read-todos
  []
  (sql/with-connection db-spec
    (sql/with-query-results res
      ["select id, text, completed from todos"]
      (doall res))))

(defn delete-todo
  [id]
  (let [deleted (sql/with-connection db-spec
                  (sql/delete-rows :todos ["id = ?" id]))]
    (if (= 1 (first deleted))
      {:status :success :result nil}
      {:status :failure :message (format "Error: Could not delete todo with id %d" id)})))

(defn update-todo
  [id text completed]
  (let [to-update {:id id :text text :completed completed}
        updated (sql/with-connection db-spec
                  (sql/update-values :todos ["id = ?" id] to-update))]
    (if (= 1 (first updated))
      {:status :success :result to-update}
      {:status :failure :message (format "Error: Could not update todo with id %d" id)})))
