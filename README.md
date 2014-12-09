## A Simple Web Service, in Clojure

This app allows you to perform CRUD operations on a single resource, a todo. The todo has the following shape:

```
Todo {
  id        integer
  text      string
  completed boolean
}
```

### Project Setup

1. Install [Leiningen](http://leiningen.org/)
2. Install Java
3. Install MySQL
4. Create a MySQL database named `tutorial_dev` and ensure you have a user `root` with an empty password. (If you don't, you'll need to update the JDBC connection strings in `project.clj`)
5. Run the migrations: `lein ragtime migrate`
5. Run the following command to start the application: `lein ring server-headless 3000`
6. Verify the application is running by running the following command: `curl -v http://localhost:3000/todos`

### Running the Tests

Basic tests have been written against the Ring interface (similar to Rack) and are located in `test/tutorial/handler_test.clj`. The application will be run using an in-memory H2 database.

To run the tests, simply do a: `lein test`.

### Anatomy of the Project

Project structure:

```
├── Procfile
├── migrations
│   ├── 20141203142800-add-todos-table.down.sql
│   └── 20141203142800-add-todos-table.up.sql
├── project.clj
├── src
│   └── tutorial
│       ├── data.clj
│       ├── handler.clj
│       ├── http.clj
│       ├── main.clj
│       └── router.clj
└── test
    └── tutorial
        └── handler_test.clj
```

1. System entry point: [`main.clj`](https://github.com/laser/web-service-clojure/blob/master/src/tutorial/main.clj)
2. Compojure API route-definitions: [`router.clj`](https://github.com/laser/web-service-clojure/blob/master/src/tutorial/router.clj)
3. HTTP request-handlers: [`handler.clj`](https://github.com/laser/web-service-clojure/blob/master/src/tutorial/handler.clj)
4. An interface wrapping JDBC, for database connectivity: [`data.clj`](https://github.com/laser/web-service-clojure/blob/master/src/tutorial/data.clj)
5. HTTP response-forming functions: [`http.clj`](https://github.com/laser/web-service-clojure/blob/master/src/tutorial/http.clj)
