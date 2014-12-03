(ns allocations.handler
  (:require [allocations.db :as db]
            [allocations.http :as http]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :as cc]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
  (:gen-class)))

(cc/defroutes app-routes
  (cc/GET "/todos" []
          (http/ok (db/read-todos)))
  (cc/GET "/todos/:id" [id]
          (http/ok (db/read-todo id)))
  (cc/POST "/todos" [:as req]
           (http/created (->> req :body keywordize-keys :text db/create-todo :id (http/url-for req))))
  (cc/ANY "*" []
          (http/not-found)))

(def app
  (-> app-routes
      handler/site
      wrap-json-body
      wrap-json-response))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port port
                            :join? false})))
