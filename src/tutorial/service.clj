(ns tutorial.service
  (:require [tutorial.db :as db]))

(defn create-todo
  [text completed]
  (let [results (db/create-todo<! {:text text :completed completed})]
    (->> results
         vals
         first
         (assoc {:text text :completed completed} :id))))

(def read-todo-by-id #(first (db/read-todos-by-id {:id %})))
(def read-todos db/read-todos)
(def delete-todo-by-id #(db/delete-todo-by-id! {:id %}))

(defn update-todo-by-id
  [id text completed]
  (let [id (read-string id)]
    (db/update-todo-by-id! {:id id :text text :completed completed})
    {:id id, :text text, :completed completed}))
