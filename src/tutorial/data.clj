(ns tutorial.data
  (:require [yesql.core :refer [defqueries]]
            [environ.core :refer [env]]))

(defqueries "sql/todos.sql" {:connection (env :database-url)})
