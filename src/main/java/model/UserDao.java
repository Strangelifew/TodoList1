package model;

import db.DBSettings;
import org.jdbi.v3.core.Handle;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserDao {

    private static int maxTodos = 10;


    private UserDao() {
    }


    public static int getMaxTodos() {
        return maxTodos;
    }

    public static void setMaxTodos(int maxTodos) {
        UserDao.maxTodos = maxTodos;
    }

    public static boolean add(User user) {
        try {
            DBSettings.withJdbiHandle(handle -> handle
                    .createUpdate("INSERT INTO users (user_id, name) VALUES(:userId, :name)")
                    .bindBean(user)
                    .execute());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<User> find(UUID id) {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM users WHERE user_id = :id")
                    .bind("id", id)
                    .mapToBean(User.class)
                    .findFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean update(UUID userId, String name) {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createUpdate("UPDATE users SET name = :name WHERE user_id = :id")
                    .bind("name", name)
                    .bind("id", userId)
                    .execute() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> freeUsers() {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM users")
                    .mapToBean(User.class)
                    .stream()
                    .filter(u -> TodoDao.getAssigned(u).size() < getMaxTodos())
                    .collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void remove(UUID userId) {
        try {
            DBSettings.withJdbiHandle(handle -> {
                TodoDao.unassign(userId, handle);
                return handle
                        .createUpdate("DELETE from users WHERE user_id = :id")
                        .bind("id", userId)
                        .execute();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> all() {
        try {
            return DBSettings.withJdbiHandle(handle -> handle
                    .createQuery("SELECT * FROM users ORDER BY name")
                    .mapToBean(User.class)
                    .list());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UUID getIdByName(String userName, Handle handle) {
        return handle
                .createQuery("SELECT * FROM users WHERE name = :name")
                .bind("name", userName)
                .mapToBean(User.class)
                .findFirst().map(User::getUserId).orElse(null);
    }
}
