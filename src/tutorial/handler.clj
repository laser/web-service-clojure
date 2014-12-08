(ns tutorial.handler
  (:require [tutorial.service :as service]
            [tutorial.http :as http]))

(defn post-todo
  [req]
  (let [b #(get-in req [:body %])
        results (service/create-todo (b :text) (b :completed))]
    (->> results
         :id
         (http/url-for req)
         (http/created results))))

(defn get-todos
  []
  (http/ok (service/read-todos)))

(defn get-todo
  [id]
  (http/ok (service/read-todo-by-id id)))

(defn patch-todo
  [body params]
  (http/ok (service/update-todo-by-id (:id params) (:text body) (:completed body))))

(defn delete-todo
  [id]
  (service/delete-todo-by-id id)
  (http/no-content))

(defn not-found
  []
  (http/not-found))
