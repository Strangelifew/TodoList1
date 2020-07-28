package db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseRunner {
    public static String CHANGELOG_FILE = "db/changeLog.xml";

    public static void runLiquibaseUpdate() throws SQLException, LiquibaseException, InterruptedException {
        boolean isUpdated = false;
        while (!isUpdated) {
            try (Connection c = DBSettings.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
                new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database).update("main");
                isUpdated = true;
            } catch (PSQLException e) {
                if (e.getMessage().contains("the database system is starting up")) Thread.sleep(1000);
                else throw e;
            }
        }
    }
}
