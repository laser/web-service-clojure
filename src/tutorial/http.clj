(ns tutorial.http
  (:require
            [clojure.string :refer [join]]
            [ring.util.response :refer [response status header]]))

(defn ok
  "HTTP 200 OK"
  [body]
  (->
    (response body)
    (status 200)))

(defn created
  "HTTP 201 Created"
  ([body url]
   (->
     (response body)
     (status 201)
     (header "Location" url))))

(defn no-content
  "HTTP 204 No Content"
  []
  (->
    (response nil)
    (status 204)))

(defn not-found
  "HTTP 404 Not Found"
  []
  (->
    (response nil)
    (status 404)))

(defn internal-error
  "HTTP 500 Internal Error"
  []
  (->
    (response nil)
    (status 500)))

(defn- url-for-req
  "Get the fully-qualified URL of a Ring request"
  [{scheme :scheme server-name :server-name server-port :server-port uri :uri}]
  (->
    scheme
    str
    (subs 1)
    (str "://" server-name ":" server-port uri)))

(defn url-for
  "Create an URL given a Ring request + things to add to the path"
  [req & path-parts]
  (join "/" (cons (url-for-req req) path-parts)))
