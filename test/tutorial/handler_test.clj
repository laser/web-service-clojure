(ns tutorial.handler_test
  (:require [clojure.test :refer :all]
            [tutorial.main :refer [app]]
            [ring.mock.request :refer [request content-type]]
            [clojure.data.json :refer [write-str read-str]]
            [environ.core :refer [env]]
            [ragtime.core :refer [migrate-all connection]]
            [ragtime.sql.files :refer [migrations]]))

(defn with-test-db
  [f]
  (migrate-all (connection (str "jdbc:" (env :database-url))) (migrations))
  (f))

(use-fixtures :each with-test-db)

(defn responsify
  ([] (responsify 200))
  ([code] (responsify code nil))
  ([code body] (responsify code body {}))
  ([code body headers] (let [defaults {:status code :body body :headers {}}]
                         (if (nil? body)
                           defaults
                           (->> {"Content-Type" "application/json; charset=utf-8"}
                                (merge headers)
                                (hash-map :headers)
                                (merge defaults))))))

(defn api-req
  [& args]
  (app (content-type (apply request args) "application/json")))

(def api-post #(api-req :post %1 (write-str %2)))
(def api-patch #(api-req :patch %1 (write-str %2)))
(def api-get (partial api-req :get))
(def api-delete (partial api-req :delete))

(defn parse-resp
  [resp]
  (update-in resp [:body] #(if % (read-str %) nil)))

(deftest handler-lifecycle
  ;; missing route
  (is (= (responsify 404)
         (parse-resp (api-get "/doc/10"))))

  ;; create a todo
  (let [user-data {"text" "foo" "completed" false }]
    (is (= (responsify 201 (assoc user-data "id" 1) {"Location" "http://localhost:80/todos/1"})
           (parse-resp (api-post "/todos" user-data)))))

  ;; update a todo
  (let [user-data {"text" "bar" "completed" true "id" 1}]
    (is (= (responsify 200 user-data)
           (parse-resp (api-patch "/todos/1" user-data)))))

  ;; read all todos
  (let [user-data {"text" "bar" "completed" true "id" 1}]
    (is (= (responsify 200 [user-data])
           (parse-resp (api-get "/todos")))))

  ;; read a single todo
  (let [user-data {"text" "bar" "completed" true "id" 1}]
    (is (= (responsify 200 user-data)
           (parse-resp (api-get "/todos/1")))))

  ;;; delete a todo
  (is (= (responsify 204)
         (parse-resp (api-delete "/todos/1")))))
