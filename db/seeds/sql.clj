(ns seeds.sql
  (:require [clojure.java.jdbc :as j]))

(defn run [target]
  (j/with-db-connection [conn (:url (:db target))]
    (j/insert! conn :todos [:text :completed] ["Call Mother" false])))
