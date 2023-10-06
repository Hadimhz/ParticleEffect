package dev.hadimhz.particles.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariDatabase implements Database {

    private final HikariDataSource source;

    public HikariDatabase(String host, String database, String port, String user, String password) {
        source = new HikariDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setMaximumPoolSize(10);
        source.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false");
        source.setUsername(user);
        source.setPassword(password);
        source.addDataSourceProperty("autoReconnect", "true");
    }

    @Override
    public void close() {
        source.close();
    }

    public HikariDataSource getSource() {
        return source;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }

}