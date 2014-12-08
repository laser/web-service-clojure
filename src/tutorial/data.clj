(ns tutorial.data
  (:require [oj.core :as oj]
            [oj.modifiers :as db]
            [environ.core :refer [env]]))

(def db-spec (env :database-url))

(def todo-query (db/query :todos))

(defn create-todo<!
  [text completed]
  (let [user-data {:text text :completed completed}
        results (-> todo-query
                    (db/insert user-data)
                    (oj/exec db-spec))]
    (->> results first vals first (assoc user-data :id))))

(defn read-todos-by-id
  [id]
  (-> todo-query
      (db/select [:id :text :completed])
      (db/where {:id id})
      (oj/exec db-spec)))

(defn read-todos
  []
  (-> todo-query
      (db/select [:id :text :completed])
      (oj/exec db-spec)))

(defn delete-todo-by-id!
  [id]
  (oj/exec {:table :todos :delete true :where {:id id}} db-spec))

(defn update-todo-by-id!
  [id text completed]
  (let [user-data {:id id :text text :completed completed}
        updated (-> todo-query
                    (db/where {:id id})
                    (db/update user-data)
                    (oj/exec db-spec))]
    user-data))
