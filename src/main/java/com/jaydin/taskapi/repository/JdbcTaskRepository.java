package com.jaydin.taskapi.repository;

import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.service.DatabaseConnectionProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class JdbcTaskRepository implements TaskRepository{

    private final DatabaseConnectionProvider connectionProvider;

    public JdbcTaskRepository(DatabaseConnectionProvider connectionProvider){
        this.connectionProvider = connectionProvider;
    }
    @PostConstruct
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
    public boolean deleteById(int id) {
        String deleteSql = "DELETE FROM Tasks WHERE ID = ?";
        try (Connection conn = connectionProvider.getConnection();
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql))   {
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException e){
            throw new RuntimeException("Failed to delete Task", e);
        }
    }

    @Override
    public Task update(Task task){
        String updateSql = "UPDATE Tasks SET TITLE = ?, DESCRIPTION = ?, COMPLETED = ? WHERE ID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setString(1, task.getTitle());
            updateStmt.setString(2, task.getDescription());
            updateStmt.setBoolean(3, task.isCompleted());
            updateStmt.setInt(4, task.getId());
            return updateStmt.executeUpdate() == 1 ? task : null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update task", e);
        }
    }



}

