(ns tutorial.db
  (:require [yesql.core :refer [defquery]]
            [environ.core :refer [env]]))

(def db-spec
  {:connection-uri (str "jdbc:" (env :database-url))})

(defquery create-todo2<! "sql/create-todo.sql")
(defquery read-todos2 "sql/read-todos.sql")
(defquery read-todos-by-id2 "sql/read-todos-by-id.sql")
(defquery update-todo-by-id2! "sql/update-todo-by-id.sql")
(defquery delete-todo-by-id2! "sql/delete-todo-by-id.sql")

(defn create-todo
  [text completed]
  (let [results (create-todo2<! db-spec text completed)]
    (->> results
         vals
         first
         (assoc {:text text :completed completed} :id))))

(defn read-todo
  [id]
  (first (read-todos-by-id2 db-spec id)))

(defn read-todos
  []
  (read-todos2 db-spec))

(defn delete-todo
  [id]
  (delete-todo-by-id2! db-spec id))

(defn update-todo
  [id text completed]
  (let [id (read-string id)]
    (update-todo-by-id2! db-spec text completed id)
    {:id id, :text text, :completed completed}))
