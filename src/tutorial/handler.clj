(ns tutorial.handler
  (:require [compojure.core :as cc]
            [environ.core :refer [env]]
            [ragtime.core :refer [migrate-all connection]]
            [ragtime.sql.files :refer [migrations]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [tutorial.db :as db]
            [tutorial.http :as http])
  (:gen-class))

(cc/defroutes app-routes
  (cc/GET "/todos" [] (http/ok (db/read-todos)))
  (cc/GET "/todos/:id" [id] (http/ok (db/read-todo id)))
  (cc/POST "/todos" [:as req]
           (let [b #(get-in req [:body %])
                 results (db/create-todo (b :text) (b :completed))]
             (->> results
                  :id
                  (http/url-for req)
                  (http/created results))))
  (cc/DELETE "/todos/:id" [id]
             (db/delete-todo id)
             (http/no-content))
  (cc/PATCH "/todos/:id" {body :body params :params}
            (http/ok (db/update-todo (:id params) (:text body) (:completed body))))
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
