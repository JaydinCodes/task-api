package com.jaydin.taskapi;

import com.jaydin.taskapi.controller.TaskController;
import com.jaydin.taskapi.repository.InMemoryTaskRepository;
import com.jaydin.taskapi.repository.TaskRepository;
import com.jaydin.taskapi.service.TaskService;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
        // 1. Start the web server on port 8080
        Javalin app = Javalin.create().start(8080);

        // 2. Wire up your dependencies (Dependency Injection)
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);
        TaskController controller = new TaskController(service);

        // 3. Register your HTTP routes
        controller.registerRoutes(app);

        System.out.println("🚀 Task API is running at http://localhost:8080/tasks");
    }
}