(ns allocations.handler
  (:require [allocations.db :as db]
            [clojure.string :refer [upper-case join]]
            [ring.mock.request :refer [header]]
            [clojure.walk :refer [keywordize-keys]]
            [compojure.core :as cc]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response status]]
  (:gen-class)))

(defn ok
  "HTTP 200 OK"
  [body]
  (->
    (response body)
    (status 200)))

(defn created
  "HTTP 201 Created"
  ([url]
   (created url nil))
  ([url body]
   (->
     (response body)
     (status 201)
     (header "Location" url))))

(defn url-from
  "Create a location URL from request data and additional path elements"
  [{scheme :scheme server-name :server-name server-port :server-port uri :uri} & path-elements]
  (str "http://" server-name ":" server-port uri "/" (join "/" path-elements)))

(cc/defroutes app-routes
  (cc/GET "/locations" []
          (ok (db/read-locations)))
  (cc/GET "/locations/:id" [id]
          (ok (db/read-location id)))
  (cc/POST "/locations" [:as req]
           (let [{:keys [x y]} (keywordize-keys (req :body))
                 loc-id (db/create-location x y)]
             (created (url-from req loc-id)))))

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
    (jetty/run-jetty #'app {:port  port
                          :join? false})))
