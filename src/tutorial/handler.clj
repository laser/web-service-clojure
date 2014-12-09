(ns tutorial.handler
  (:require [tutorial.data :as data]
            [tutorial.http :as http]))


(defn patch-todo
  [{text :text completed :completed :as body} {id :id}]
  (let [id (read-string id)]
    (http/ok (do (data/update-todo-by-id! id text completed)
                 (assoc body :id id)))))

(defn post-todo
  [{{text :text completed :completed :as body} :body :as req}]
  (let [results (->> (data/create-todo<! text completed)
                     vals
                     first
                     (assoc body :id))]
    (->> results
         :id
         (http/url-for req)
         (http/created results))))

(def delete-todo #(do (data/delete-todo-by-id! %) (http/no-content)))
(def get-todos #(http/ok (data/read-todos)))
(def get-todo #(http/ok (first (data/read-todos-by-id %))))
(def not-found http/not-found)
