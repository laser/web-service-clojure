(ns tutorial.service
  (:require [tutorial.data :as data]))

(defn create-todo
  [text completed]
  (let [results (data/create-todo<! {:text text :completed completed})]
    (->> results
         vals
         first
         (assoc {:text text :completed completed} :id))))

(def read-todo-by-id #(first (data/read-todos-by-id {:id %})))
(def read-todos data/read-todos)
(def delete-todo-by-id #(data/delete-todo-by-id! {:id %}))

(defn update-todo-by-id
  [id text completed]
  (let [id (read-string id)]
    (data/update-todo-by-id! {:id id :text text :completed completed})
    {:id id, :text text, :completed completed}))
