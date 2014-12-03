(ns seeds.sql
  (:require [clojure.java.jdbc :as j]))

(defn run [target & args]
  (j/with-connection {:connection-uri (-> target :db :url)}
    (j/insert-values :todos [:text :completed] ["Call Mother" false] ["Buy a delicious ham" false])))
