(ns tutorial.handler_test
  (:require [clojure.test :refer :all]
            [tutorial.handler :refer :all]
            [ring.mock.request :refer [request content-type]]
            [clojure.data.json :as json]
            [ring.util.io :as io]
            [environ.core :refer [env]]
            [joplin.core :as joplin]
            [joplin.jdbc.database]))

(def db-spec
  {:connection-uri (env :database-url)})

(defn with-test-db
  [f]
  (joplin/reset-db {:db {:type :sql :url (env :database-url)}
                    :migrator "db/migrations"})
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

(def api-post #(api-req :post %1 (json/write-str %2)))
(def api-patch #(api-req :patch %1 (json/write-str %2)))
(def api-get (partial api-req :get))
(def api-delete (partial api-req :delete))

(defn parse-resp
  [resp]
  (update-in resp [:body] #(if % (json/read-str %) nil)))

(deftest handler-lifecycle
  ;;; missing route
  (is (= (responsify 404)
         (parse-resp (api-get "/doc/10"))))

  ;;; create a todo
  (let [user-data {"text" "foo" "completed" false }]
    (is (= (responsify 201 (assoc user-data "id" 1) {"Location" "http://localhost:80/todos/1"})
           (parse-resp (api-post "/todos" user-data)))))

  ;;; update a todo
  (let [user-data {"text" "bar" "completed" true "id" 1}]
    (is (= (responsify 200 user-data)
           (parse-resp (api-patch "/todos/1" user-data)))))

  ;;; read all todos
  (let [user-data {"text" "bar" "completed" true "id" 1}]
    (is (= (responsify 200 [user-data])
           (parse-resp (api-get "/todos")))))

  ;;; read a single todo
  (let [user-data {"text" "bar" "completed" true "id" 1}]
    (is (= (responsify 200 user-data)
           (parse-resp (api-get "/todos/1")))))

  ;;; delete a todo
  (is (= (responsify 204)
         (parse-resp (api-delete "/todos/1")))))
