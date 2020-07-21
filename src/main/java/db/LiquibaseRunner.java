package db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseRunner {
    public static String CHANGELOG_FILE = "db/changeLog.xml";

    public static void runLiquibaseUpdate() throws SQLException, LiquibaseException {
        try (Connection c = DBSettings.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
            new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database).update("main");
        }
    }
}
