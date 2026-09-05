package com.jaydin.taskapi.repository;

import com.jaydin.taskapi.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcTaskRepository implements TaskRepository{

    private  String jdbcUrl = "jdbc:h2:mem:testdb";
    private String username = "sa";
    private  String password = "ps";


    public JdbcTaskRepository(){

    }
    public void init() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Tasks(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "TITLE VARCHAR(50) NOT NULL, " +
                "DESCRIPTION TEXT NOT NULL, " +
                "COMPLETED BOOLEAN NOT NULL DEFAULT FALSE, " +
                "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement createStmt = conn.prepareStatement(createTableSql)) {
            createStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize Tasks table", e);
        }
    }

    @Override
    public Task save(Task task) {
        String insertSql = "INSERT INTO Tasks (TITLE, DESCRIPTION, COMPLETED) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, task.getTitle());
            insertStmt.setString(2, task.getDescription());
            insertStmt.setBoolean(3, task.isCompleted());
            insertStmt.executeUpdate();

            try (ResultSet keys = insertStmt.getGeneratedKeys()) {
                if (keys.next()) {
                    task.setId(keys.getInt(1));
                }
            }
            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save task", e);
        }
    }

    @Override
    public Task findById(int id) {
        return null;
    }

    @Override
    public List<Task> findAll() {
        return Collections.emptyList();
    }

    @Override
    public void deleteById(int id) {

    }



}

