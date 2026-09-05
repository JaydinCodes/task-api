package com.jaydin.taskapi.service;

import com.jaydin.taskapi.dto.CreateTaskRequest;
import com.jaydin.taskapi.dto.UpdateTaskRequest;
import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.repository.TaskRepository;

import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        if (taskRepository == null){
            throw new IllegalArgumentException(
                    "Task repository cannot be null"
            );
        }

        this.taskRepository = taskRepository;
    }

    public Task createTask(
            CreateTaskRequest request) {

        Task task = new Task(0,request.getTitle(),request.getDescription(), request.isCompleted(), request.getTime());

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    public Task getTaskById(int id) {

        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new IllegalArgumentException("Task with ID " + id + " not found");
        }
        return task;
    }

    public Task updateTask(
            int id,
            UpdateTaskRequest request) {

        Task task = getTaskById(id);
        task.update(request.getTitle(), request.getDescription());
        return taskRepository.save(task);
    }

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }

    public Task completeTask(int id) {
        Task task = taskRepository.findById(id);
        task.isCompleted();
        return taskRepository.save(task);
    }

}
