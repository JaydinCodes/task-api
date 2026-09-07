package com.jaydin.taskapi.controller;

import com.jaydin.taskapi.dto.CreateTaskRequest;
import com.jaydin.taskapi.dto.UpdateTaskRequest;
import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }
    @GetMapping public List<Task> findAll() { return taskService.getAllTasks(); }
    @GetMapping("/{id}") public Task getTaskById(@PathVariable int id) { return taskService.getTaskById(id); }
    @PostMapping public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request)); }
    @PutMapping("/{id}") public Task update(@PathVariable int id, @Valid @RequestBody UpdateTaskRequest request) { return taskService.updateTask(id, request); }
    @PatchMapping("/{id}/complete") public Task completeTask(@PathVariable int id) { return taskService.completeTask(id); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deleteTask(@PathVariable int id) { taskService.deleteTask(id); return ResponseEntity.noContent().build(); }
    @PutMapping("/{id}/replace") public Task replaceTask(@PathVariable int id, @Valid @RequestBody CreateTaskRequest request) { return taskService.replaceTask(id, request); }
}
