(ns tutorial.handler
  (:require [tutorial.db :as db]
            [tutorial.http :as http]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :as cc]
            [compojure.handler :as hdlr]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            (:gen-class)))

(cc/defroutes app-routes
  (cc/GET "/todos" []
          (http/ok (db/read-todos)))
  (cc/GET "/todos/:id" [id]
          (let [result (db/read-todo id)]
            (case (:status result)
              :failure (http/not-found)
              :success (http/ok (:result result)))))
  (cc/POST "/todos" [:as req]
           (let [{:keys [text completed]} (keywordize-keys (req :body))
                 result (db/create-todo text completed)]
             (case (:status result)
               :failure (http/internal-error)
               :success (->> result :result :id (http/url-for req) http/created))))
  (cc/DELETE "/todos/:id" [id]
             (let [result (db/delete-todo id)]
               (case (:status result)
                 :failure (http/not-found)
                 :success (http/no-content))))
  (cc/PATCH "/todos/:id" {body :body params :params}
           (let [{:keys [text completed]} (keywordize-keys body)
                 result (db/update-todo (:id params) text completed)]
              (case (:status result)
                :failure (http/not-found)
                :success (->> result :result http/ok))))
  (cc/ANY "*" []
          (http/not-found)))

(def app
  (-> app-routes
      hdlr/site
      wrap-json-body
      wrap-json-response))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port port
                            :join? false})))
