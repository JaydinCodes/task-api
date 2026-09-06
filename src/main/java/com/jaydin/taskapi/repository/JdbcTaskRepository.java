package com.jaydin.taskapi.repository;

import com.jaydin.taskapi.model.Task;

import java.sql.*;
import java.util.ArrayList;
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
        String fetchSQL = "SELECT * FROM Tasks WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            PreparedStatement fetchStmt = conn.prepareStatement(fetchSQL)){
            fetchStmt.setInt(1, id);

            try(ResultSet rs = fetchStmt.executeQuery()){
                if (rs.next()){
                    int taskId = rs.getInt("ID");
                    String title = rs.getString("TITLE");
                    String des = rs.getString("DESCRIPTION");
                    Boolean completed = rs.getBoolean("COMPLETED");
                    Timestamp time = rs.getTimestamp("CREATED_AT");
                    Task task = new Task(
                            taskId, title, des, completed, time
                    );
                    return task;
                }
            }
            return null;
        } catch ( SQLException e) {
            throw new RuntimeException("Failed to find task", e);
        }

    }

    @Override
    public List<Task> findAll() {
        String findAllSql = "SELECT * FROM Tasks";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            PreparedStatement findStmt = conn.prepareStatement(findAllSql)){
            try(ResultSet rs = findStmt.executeQuery()){

                while (rs.next()){
                    int taskId = rs.getInt("ID");
                    String title = rs.getString("TITLE");
                    String des = rs.getString("DESCRIPTION");
                    Boolean completed = rs.getBoolean("COMPLETED");
                    Timestamp time = rs.getTimestamp("CREATED_AT");
                    Task task = new Task(
                            taskId, title, des, completed, time
                    );
                    tasks.add(task);

                }
            }
            return tasks;
        } catch (SQLException e){
            throw new RuntimeException("Empty List");
        }
    }

    @Override
    public void deleteById(int id) {
        String deleteSql = "DELETE FROM Tasks WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql))   {
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Failed to delete Task");
        }
    }

    public Task update(Task task) {
        String updateSql = "UPDATE Tasks SET TITLE = ?, DESCRIPTION = ?, COMPLETED = ? WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement updateStmt = conn.prepareStatement(updateSql)){
            updateStmt.setString(1, task.getTitle());
            updateStmt.setString(2, task.getDescription());
            updateStmt.setBoolean(3, task.isCompleted());
            updateStmt.setInt(4, task.getId());

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No task found with ID: " + task.getId());
            }

            return task;


        } catch (SQLException e) {
            throw new RuntimeException("Failed to update task");
        }

    }



}

