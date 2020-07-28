package db;

import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface DBSettings {
    String USER_NAME = "postgres";
    String PASSWORD = "postgres";
    String URL = "jdbc:postgresql://db:5432/TodoList";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

    static <T, X extends Exception> T withJdbiHandle(HandleCallback<T, X> callback) throws SQLException, X {
        try (Connection c = DBSettings.getConnection()) {
            return Jdbi.create(c).withHandle(callback);
        }
    }
}
