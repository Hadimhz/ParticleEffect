package dev.hadimhz.particles.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface Database {

    void close();

    Connection getConnection() throws SQLException;

    default void createTable(String table, String column) {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS `" + table + "` (" + column + ");");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}