(ns allocations.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload])
  (:gen-class))

(defn handler [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello folks"})

(defn -main [& args]
  (run-server (reload/wrap-reload handler) {:port 8080}))
