package com.jaydin.taskapi.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
public class DatabaseConnectionProvider implements AutoCloseable{

    private final HikariDataSource dataSource;
    public DatabaseConnectionProvider(String jdbcUrl, String username, String password){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30_000);
        config.setIdleTimeout(600_000);

        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        dataSource.close();
    }



}
