(ns tutorial.service
  (:require [tutorial.data :as data]))

(defn create-todo
  [text completed]
  (let [results (data/create-todo<! text completed)]
    (->> results
         vals
         first
         (assoc {:text text :completed completed} :id))))

(def read-todo-by-id #(first (data/read-todos-by-id %)))
(def read-todos data/read-todos)
(def delete-todo-by-id data/delete-todo-by-id!)

(defn update-todo-by-id
  [id text completed]
  (data/update-todo-by-id! id text completed)
  {:id id, :text text, :completed completed})
