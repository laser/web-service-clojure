# web-service-clojure

Build a simple RESTful API in Clojure.

## things that I did

1. Do all the Clojure koans
1. Responding to HTTP requests w/static JSON
  1. Create a new Leiningen project
  1. Add Compojure, Ring (defaults, Jetty adapter, JSON)
  1. Add some routes and have them return static JSON
  1. Make sure you use the right status codes and set the Location header
1. Connecting to a database
  1. Add JDBC, H2, Joplin, and oj
  1. Create migrations and seeds and then run them
  1. Connect your handler's routes to the database

## miscellaneous topics

1. Deploying to Heroku
  1. Using different databases in different environments
  1. Using PostgreSQL in prod, H2 in dev
  1. Creating a Procfile
  1. Making sure your Joplin migrations run
1. Testing
  1. Unit testing your functions
  1. HTTP-level testing
1. Exception handling
