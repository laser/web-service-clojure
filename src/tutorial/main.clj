(ns tutorial.main
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [compojure.core :as cc]
            [tutorial.router :as router])
  (:gen-class))

(cc/defroutes app-routes router/routes)

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-json-response))

(defn start
  [port]
  (jetty/run-jetty app {:port port
                        :join? false}))

(defn -main
  []
  (let [port (Integer. (or (System/getenv "PORT") "3000"))]
    (start port)))
