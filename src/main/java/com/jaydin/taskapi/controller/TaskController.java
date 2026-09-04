package com.jaydin.taskapi.controller;

import com.jaydin.taskapi.dto.CreateTaskRequest;
import com.jaydin.taskapi.dto.UpdateTaskRequest;
import com.jaydin.taskapi.service.TaskService;
import io.javalin.Javalin;

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    public void registerRoutes(Javalin app) {

        app.get("/tasks", ctx -> {
            ctx.json(taskService.getAllTasks());
        });

        // GET /tasks/{id} -> Returns a specific task
        app.get("/tasks/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(taskService.getTaskById(id));
        });

        // POST /tasks -> Creates a new task
        app.post("/tasks", ctx -> {
           CreateTaskRequest request = ctx.bodyAsClass(CreateTaskRequest.class);
           ctx.status(201).json(taskService.createTask(request));
        });

        // PUT /tasks/{id} -> Updates a task
        app.put("/tasks/{id}", ctx -> {
           UpdateTaskRequest request = ctx.bodyAsClass(UpdateTaskRequest.class);
           int id = Integer.parseInt(ctx.pathParam("id"));
           ctx.json(taskService.updateTask(id, request));
        });

        // PATCH /tasks/{id}/complete -> Marks a task as complete

        app.patch("/tasks/{id}/complete", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(taskService.completeTask(id));
        });

        // DELETE /tasks/{id} -> Deletes a task
        app.delete("/tasks/{id}", ctx -> {
           int id = Integer.parseInt(ctx.pathParam("id"));
           taskService.deleteTask(id);
           ctx.status(204);
        });
    }

}
