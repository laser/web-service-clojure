-- name: create-todo<!
INSERT INTO todos (text, completed)
VALUES (:text, :completed)

-- name: read-todos-by-id
SELECT id, text, completed
FROM todos
WHERE todos.id = :id

-- name: read-todos
SELECT id, text, completed
FROM todos;

-- name: update-todo-by-id!
UPDATE todos
SET text = :text, completed = :completed
WHERE id = :id

-- name: delete-todo-by-id!
DELETE FROM todos
WHERE id = :id
