package model;

import db.DBSettings;
import org.jdbi.v3.core.Handle;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static model.Status.ACTIVE;
import static model.Status.COMPLETE;

public final class TodoDao {
    private TodoDao() {
    }

    public static void add(Todo todo) {
        try {
            DBSettings.withJdbiHandle(handle -> handle
                    .createUpdate("INSERT INTO todos (todo_id, title, status) VALUES(:todoId, :title, :status)")
                    .bindBean(todo)
                    .execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Optional<Todo> find(UUID todoId) {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM todos WHERE todo_id = :id")
                    .bind("id", todoId)
                    .mapToBean(Todo.class)
                    .findFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(UUID todoId, String title) {
        try {
            DBSettings.withJdbiHandle(handle -> handle
                    .createUpdate("UPDATE todos SET title = :title WHERE todo_id = :todoId")
                    .bind("title", title)
                    .bind("todoId", todoId)
                    .execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Todo> ofStatus(String statusString) {
        return (statusString == null || statusString.isEmpty()) ? all() : ofStatus(Status.valueOf(statusString.toUpperCase()));
    }

    public static List<Todo> ofStatus(Status status) {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM todos WHERE status = :status")
                    .bind("status", status)
                    .mapToBean(Todo.class)
                    .list());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void remove(UUID todoId) {
        try {
            DBSettings.withJdbiHandle(handle -> handle
                    .createUpdate("DELETE FROM todos WHERE todo_id = :id")
                    .bind("id", todoId)
                    .execute());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeCompleted() {
        try {
            DBSettings.withJdbiHandle(handle -> handle
                    .createUpdate("DELETE FROM todos WHERE status = :status")
                    .bind("status", COMPLETE)
                    .execute());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void toggleStatus(UUID todoId) {
        try {
            DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM todos WHERE todo_id = :id")
                    .bind("id", todoId)
                    .mapToBean(Todo.class)
                    .findFirst()
                    .map(todo -> handle
                            .createUpdate("UPDATE todos SET status = :status WHERE todo_id = :todoId")
                            .bind("status", todo.getStatus() == COMPLETE ? ACTIVE : COMPLETE)
                            .bind("todoId", todoId)
                            .execute()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void toggleAll(boolean complete) {
        try {
            DBSettings.withJdbiHandle(handle -> handle
                    .createUpdate("UPDATE todos SET status = :status")
                    .bind("status", complete ? COMPLETE : ACTIVE)
                    .execute());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Todo> all() {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM todos")
                    .mapToBean(Todo.class)
                    .list());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Todo> getAssigned(User user) {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM todos WHERE user_id = :userId")
                    .bind("userId", user.getUserId())
                    .mapToBean(Todo.class)
                    .list());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void assign(String userName, UUID todoId) {
        try {
            DBSettings.withJdbiHandle(handle -> {
                boolean isUpdated = handle
                        .createUpdate("UPDATE todos SET user_id = :userId WHERE todo_id = :todoId")
                        .bind("userId", UserDao.getIdByName(userName, handle))
                        .bind("todoId", todoId)
                        .execute() > 0;
                if (!isUpdated) {
                    throw new IllegalStateException("Failed to find todo by id = " + todoId);
                }
                return null;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unassign(UUID userId, Handle handle) {
        handle
                .createUpdate("UPDATE todos SET user_id = NULL WHERE user_id = :userId")
                .bind("userId", userId)
                .execute();
    }
}