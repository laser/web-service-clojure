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
  (cc/GET "/locations" []
          (http/ok (db/read-locations)))
  (cc/GET "/locations/:id" [id]
          (http/ok (db/read-location id)))
  (cc/POST "/locations" [:as req]
           (let [{:keys [x y]} (keywordize-keys (req :body))
                 loc-id (db/create-location x y)
                 loc-url (http/url-for req loc-id)]
             (http/created loc-url)))
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
