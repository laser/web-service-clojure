(ns tutorial.db
  (:require [oj.core :as oj]
            [oj.modifiers :as db]
            [environ.core :refer [env]]))

(def db-spec
  {:connection-uri (env :database-url)})

(defn create-todo
  [text completed]
  (let [user-data {:text text :completed completed}
        results (-> (db/query :todos)
                    (db/insert user-data)
                    (oj/exec db-spec))]

    (if (= 1 (count results))
      {:status :success :result (->> results vals first (assoc user-data :id))}
      {:status :failure :message (format "Error: Could not create todo with text %s" text)})))

(defn read-todo
  [id]
  (let [results (-> (db/query :todos)
                    (db/select [:id :text :completed])
                    (db/where {:id id})
                    (oj/exec db-spec))]

    (if (= 1 (count results))
      {:status :success :result (first results)}
      {:status :failure :message (format "Error: No todo found with id %s" id)})))

(defn read-todos
  []
  (println db-spec)
  (-> (db/query :todos)
      (db/select [:id :text :completed])
      (oj/exec db-spec)))

(defn delete-todo
  [id]
  (let [deleted (-> {:table :todos :delete true :where {:id id}}
                    (oj/exec db-spec))]

    (if (= 1 (first deleted))
      {:status :success :result nil}
      {:status :failure :message (format "Error: Could not delete todo with id %s" id)})))

(defn update-todo
  [id text completed]
  (let [user-data {:id id :text text :completed completed}
        updated (-> (db/query :todos)
                    (db/where {:id id})
                    (db/update user-data)
                    (oj/exec db-spec))]

    (if (= 1 (first updated))
      {:status :success :result user-data}
      {:status :failure :message (format "Error: Could not update todo with id %d" id)})))
