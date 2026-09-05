package com.jaydin.taskapi;

import com.jaydin.taskapi.model.Task;

import java.sql.*;
import java.time.LocalDateTime;

public class random {
    public static void main(String args[])  {
        String jdbcUrl = "jdbc:h2:mem:testdb";
        String username = "sa";
        String password = "ps";

        try (Connection conn = DriverManager.getConnection(
                jdbcUrl,
                username,
                password
        )) {
            System.out.println("Connected h2 in-memory database");

            String createTableSql = "CREATE TABLE Tasks(ID INT AUTO_INCREMENT PRIMARY KEY, TITLE VARCHAR(50) NOT NULL, DESCRIPTION TEXT NOT NULL, COMPLETED BOOLEAN NOT NULL DEFAULT FALSE, CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            try (PreparedStatement createStmt = conn.prepareStatement(createTableSql)) {
                createStmt.executeUpdate();
                System.out.println("Table: Tasks created");
            }

            String insertSql = "INSERT INTO Tasks (TITLE, DESCRIPTION, COMPLETED) VALUES (?, ? ,?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, "Learn JDBC");
                insertStmt.setString(2, "Jaydin is learning JDBC");
                insertStmt.setBoolean(3, true);
                insertStmt.executeUpdate();

                System.out.println("Data inserted.");
            }

            String selectSql = "SELECT * FROM Tasks";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql); ResultSet rs = selectStmt.executeQuery()) {

                System.out.println("--- Task list ---");
                while (rs.next()){
                    int id = rs.getInt("ID");
                    String title = rs.getString("TITLE");
                    String des = rs.getString("DESCRIPTION");
                    Boolean isCompleted = rs.getBoolean("COMPLETED");
                    LocalDateTime currentTime = rs.getObject("CREATED_AT", LocalDateTime.class);
                    Task task = new Task(
                            id,
                            title,
                            des,
                            isCompleted,
                            currentTime
                    );
                    System.out.println(task);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}