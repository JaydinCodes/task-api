package com.jaydin.taskapi.service;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
public class DatabaseConnectionProvider {

    private  String jdbcUrl = "jdbc:h2:mem:testdb";
    private String username = "sa";
    private  String password = "ps";
    public DatabaseConnectionProvider(String jdbcUrl, String username, String password){
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(jdbcUrl, username, password);
    }



}
