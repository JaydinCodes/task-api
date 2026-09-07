package com.jaydin.taskapi.repository;

import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.service.DatabaseConnectionProvider;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcTaskRepository implements TaskRepository{


    private final DatabaseConnectionProvider connectionProvider;

    public JdbcTaskRepository(DatabaseConnectionProvider connectionProvider){
        this.connectionProvider = connectionProvider;
    }
    public void init() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Tasks(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "TITLE VARCHAR(50) NOT NULL, " +
                "DESCRIPTION TEXT NOT NULL, " +
                "COMPLETED BOOLEAN NOT NULL DEFAULT FALSE, " +
                "CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";


        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement createStmt = conn.prepareStatement(createTableSql)) {
            createStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize Tasks table", e);
        }
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        int taskId = rs.getInt("ID");
        String title = rs.getString("TITLE");
        String des = rs.getString("DESCRIPTION");
        Boolean completed = rs.getBoolean("COMPLETED");
        LocalDateTime time = rs.getTimestamp("CREATED_AT").toLocalDateTime();
        return new Task(taskId, title, des, completed, time);
    }

    @Override
    public Task save(Task task) {
        String insertSql = "INSERT INTO Tasks (TITLE, DESCRIPTION, COMPLETED) VALUES (?, ?, ?)";

        try (Connection conn = connectionProvider.getConnection();
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
        try (Connection conn = connectionProvider.getConnection();
            PreparedStatement fetchStmt = conn.prepareStatement(fetchSQL)){
            fetchStmt.setInt(1, id);

            try(ResultSet rs = fetchStmt.executeQuery()){
                if (rs.next()){
                    return mapRowToTask(rs);
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
        try (Connection conn = connectionProvider.getConnection();
            PreparedStatement findStmt = conn.prepareStatement(findAllSql)){
            try(ResultSet rs = findStmt.executeQuery()){

                while (rs.next()){
                    tasks.add(mapRowToTask(rs));
                }
            }
            return tasks;
        } catch (SQLException e){
            throw new RuntimeException("Failed to find all tasks");
        }
    }

    @Override
    public void deleteById(int id) {
        String deleteSql = "DELETE FROM Tasks WHERE ID = ?";
        try (Connection conn = connectionProvider.getConnection();
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql))   {
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Failed to delete Task", e);
        }
    }

    @Override
    public Task replaceTask(Task oldTask, Task newTask){
        String deleteSql = "DELETE FROM Tasks WHERE ID = ?";
        String insertSql = "INSERT INTO Tasks (TITLE, DESCRIPTION, COMPLETED) VALUES (?, ?, ?)";
        try(Connection conn = connectionProvider.getConnection()){
            conn.setAutoCommit(false); // Begin the Transaction
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)){
                    deleteStmt.setInt(1, oldTask.getId());
                    deleteStmt.executeUpdate();

                    insertStmt.setString(1, newTask.getTitle());
                    insertStmt.setString(2, newTask.getDescription());
                    insertStmt.setBoolean(3, newTask.isCompleted());
                    insertStmt.executeUpdate();

                    try(ResultSet rs = insertStmt.getGeneratedKeys()){
                        if (rs.next()){
                            newTask.setId(rs.getInt(1));
                        }
                    }
                    conn.commit(); // Commit the transaction
                    return newTask;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to replace task", e);
        }
    }



}

