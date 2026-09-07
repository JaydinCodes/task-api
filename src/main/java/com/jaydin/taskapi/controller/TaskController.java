package com.jaydin.taskapi.controller;

import com.jaydin.taskapi.dto.CreateTaskRequest;
import com.jaydin.taskapi.dto.UpdateTaskRequest;
import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.repository.JdbcTaskRepository;
import com.jaydin.taskapi.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks") // Base URL for all task-related endpoints
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> findAll(){
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id){
        Task task = taskService.getTaskById(id);
        if (task == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request){
        Task saved = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable int id, @RequestBody UpdateTaskRequest request){
        Task updateTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updateTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/replace")
    public ResponseEntity<Task> replaceTask(@PathVariable int id, @RequestBody Task task){
        Task oldTask = taskService.getTaskById(id);
        Task replaced = taskService.replaceTask(oldTask, task);
        return ResponseEntity.ok(replaced);
    }


}