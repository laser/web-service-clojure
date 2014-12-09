(ns tutorial.handler
  (:require [tutorial.data :as data]
            [tutorial.http :as http]))

(defn post-todo
  [{{text :text completed :completed} :body :as req}]
  (let [results (->> (data/create-todo<! (get-in req [:body :text]) (get-in req [:body :completed]))
                     vals
                     first
                     (assoc {:text text, :completed completed} :id))]
    (->> results
         :id
         (http/url-for req)
         (http/created results))))

(defn get-todos
  []
  (http/ok (data/read-todos)))

(defn get-todo
  [id]
  (http/ok (first (data/read-todos-by-id id))))

(defn patch-todo
  [{text :text completed :completed} {id :id}]
  (let [id (read-string id)]
    (http/ok (do (data/update-todo-by-id! id text completed)
                 {:id id, :text text, :completed completed}))))

(defn delete-todo
  [id]
  (data/delete-todo-by-id! id)
  (http/no-content))

(defn not-found
  []
  (http/not-found))
