(ns tutorial.handler
  (:require [compojure.core :as cc]
            [environ.core :refer [env]]
            [ragtime.core :refer [migrate-all connection]]
            [ragtime.sql.files :refer [migrations]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [tutorial.service :as svc]
            [tutorial.http :as http])
  (:gen-class))

(cc/defroutes app-routes
  (cc/GET "/todos" [] (http/ok (svc/read-todos)))
  (cc/GET "/todos/:id" [id] (http/ok (svc/read-todo-by-id id)))
  (cc/POST "/todos" [:as req]
           (let [b #(get-in req [:body %])
                 results (svc/create-todo (b :text) (b :completed))]
             (->> results
                  :id
                  (http/url-for req)
                  (http/created results))))
  (cc/DELETE "/todos/:id" [id]
             (svc/delete-todo-by-id id)
             (http/no-content))
  (cc/PATCH "/todos/:id" {body :body params :params}
            (http/ok (svc/update-todo-by-id (:id params) (:text body) (:completed body))))
  (cc/ANY "*" []
          (http/not-found)))

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
  (migrate-all (connection (str "jdbc:" (env :database-url))) (migrations))
  (let [port (Integer. (or (System/getenv "PORT") "3000"))]
    (start port)))
