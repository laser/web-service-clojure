(ns allocations.http
  (:require
            [clojure.string :refer [join]]
            [ring.mock.request :refer [header]]
            [ring.util.response :refer [response status]]))

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

(defn not-found
  "HTTP 404 Not Found"
  []
  (->
    (response nil)
    (status 404)))

(defn- url-for-req
  "Get the fully-qualified URL of a Ring request"
  [{scheme :scheme server-name :server-name server-port :server-port uri :uri}]
  (str "http://" server-name ":" server-port uri))

(defn url-for
  "Create an URL given a Ring request + things to add to the path"
  [req & path-parts]
  (join "/" (cons (url-for-req req) path-parts)))