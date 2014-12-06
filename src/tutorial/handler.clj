(ns tutorial.handler
  (:require [tutorial.db :as db]
            [tutorial.http :as http]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :as cc]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            (:gen-class)))

(cc/defroutes app-routes
  (cc/GET "/todos" []
          (let [work (db/read-todos)]
            (case (:status work)
              :failure (http/internal-error)
              :success (http/ok (:result work)))))
  (cc/GET "/todos/:id" [id]
          (let [work (db/read-todo id)]
            (case (:status work)
              :failure (http/not-found)
              :success (http/ok (:result work)))))
  (cc/POST "/todos" [:as req]
           (let [b #(get-in req [:body %])
                 work (db/create-todo (b :text) (b :completed))]
             (case (:status work)
               :failure (http/internal-error)
               :success (->> work
                             :result
                             :id
                             (http/url-for req)
                             (http/created (:result work))))))
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
      (wrap-json-body {:keywords? true})
      wrap-json-response))
