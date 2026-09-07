package com.jaydin.taskapi.service;

import com.jaydin.taskapi.dto.CreateTaskRequest;
import com.jaydin.taskapi.dto.UpdateTaskRequest;
import com.jaydin.taskapi.exception.TaskNotFoundException;
import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        if (taskRepository == null) throw new IllegalArgumentException("Task repository cannot be null");
        this.taskRepository = taskRepository;
    }
    public Task createTask(CreateTaskRequest request) { return taskRepository.save(new Task(0, request.getTitle(), request.getDescription(), request.isCompleted(), LocalDateTime.now())); }
    public List<Task> getAllTasks() { return taskRepository.findAll(); }
    public Task getTaskById(int id) {
        Task task = taskRepository.findById(id);
        if (task == null) throw notFound(id);
        return task;
    }
    public Task updateTask(int id, UpdateTaskRequest request) {
        Task task = getTaskById(id); task.update(request.getTitle(), request.getDescription()); return persist(task);
    }
    public Task completeTask(int id) {
        Task task = getTaskById(id); task.markCompleted(); return persist(task);
    }
    public Task replaceTask(int id, CreateTaskRequest request) {
        Task task = getTaskById(id); task.replace(request.getTitle(), request.getDescription(), Boolean.TRUE.equals(request.isCompleted())); return persist(task);
    }
    public void deleteTask(int id) { if (!taskRepository.deleteById(id)) throw notFound(id); }
    private Task persist(Task task) {
        Task updated = taskRepository.update(task);
        if (updated == null) throw notFound(task.getId());
        return updated;
    }
    private TaskNotFoundException notFound(int id) { return new TaskNotFoundException("Task with ID " + id + " was not found"); }
}
