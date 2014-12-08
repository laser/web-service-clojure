(ns tutorial.router
  (:require [tutorial.handler :as handler]
            [compojure.core :as cc]))

(cc/defroutes routes
  (cc/POST "/todos" [:as req] (handler/post-todo req))
  (cc/GET "/todos" [] (handler/get-todos))
  (cc/GET "/todos/:id" [id] (handler/get-todo id))
  (cc/PATCH "/todos/:id" {body :body params :params} (handler/patch-todo body params))
  (cc/DELETE "/todos/:id" [id] (handler/delete-todo id))
  (cc/ANY "*" [] (handler/not-found)))
