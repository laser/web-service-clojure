(ns seeds.sql
  (:require [clojure.java.jdbc :as j]))

(defn run [target & args]
  (let [db-url (-> target
                   :db
                   :url)]
    (j/with-db-connection [conn db-url]
      (j/insert! conn :todos [:text :completed] ["Call Mother" false] ["Buy a delicious ham" false]))))
