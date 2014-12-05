(ns tutorial.handler_test
  (:require [clojure.test :refer :all]
            [tutorial.handler :refer :all]
            [ring.mock.request :refer [request]]
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

(use-fixtures :once with-test-db)

(defn responsify
  ([] (responsify 200))
  ([code] (responsify code nil))
  ([code body] (responsify code body {}))
  ([code body headers] {:status code
                        :body body
                        :headers (merge {"Content-Type" "application/json; charset=utf-8"} headers)}))

(deftest handler-not-found
  (is (= (responsify 404 nil {"Content-Type" "application/json"})
         (app (request :get "/doc/10")))))

(deftest handler-get-todos-empty
  (is (= (responsify 200 (json/write-str []))
         (app (request :get "/todos")))))
