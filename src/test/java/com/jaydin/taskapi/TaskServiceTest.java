package com.jaydin.taskapi;

import com.jaydin.taskapi.dto.CreateTaskRequest;
import com.jaydin.taskapi.dto.UpdateTaskRequest;
import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.repository.InMemoryTaskRepository;
import com.jaydin.taskapi.repository.TaskRepository;

import com.jaydin.taskapi.service.TaskService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @Test
    public void createTask_createsTaskWithProvidedDetails() {
        // Arrange
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Study Java");
        request.setDescription("Focus on Dependency Injection");

        // Act
        Task task = service.createTask(request);

        // Assert
        assertNotNull(task);
        assertEquals("Study Java", task.getTitle());
        assertEquals("Focus on Dependency Injection", task.getDescription());
        assertFalse(task.isCompleted());
        assertTrue(task.getId() > 0); // Ensures the repository generated an ID
    }

    @Test
    public void createTask_rejectsBlankTitle() {
        // Arrange
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(null); // Our Task model throws NullPointerException if title is null

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            service.createTask(request);
        });
    }

    @Test
    public void getTaskById_returnsExistingTask() {
        // Arrange
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Study Java");
        Task createdTask = service.createTask(request);

        // Act
        Task fetchedTask = service.getTaskById(createdTask.getId());

        // Assert
        assertEquals(createdTask.getId(), fetchedTask.getId());
        assertEquals("Study Java", fetchedTask.getTitle());
    }

    @Test
    public void getTaskById_throwsWhenTaskDoesNotExist() {
        // Arrange
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);

        // Act & Assert
        // We expect the IllegalArgumentException we wrote in our getTaskById method
        assertThrows(IllegalArgumentException.class, () -> {
            service.getTaskById(999);
        });
    }

    @Test
    public void updateTask_changesTaskDetails() {
        // Arrange
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);

        CreateTaskRequest createReq = new CreateTaskRequest();
        createReq.setTitle("Old Title");
        Task task = service.createTask(createReq);

        UpdateTaskRequest updateReq = new UpdateTaskRequest();
        updateReq.setTitle("New Title");
        updateReq.setDescription("New Description");

        // Act
        Task updatedTask = service.updateTask(task.getId(), updateReq);

        // Assert
        assertEquals("New Title", updatedTask.getTitle());
        assertEquals("New Description", updatedTask.getDescription());
        assertEquals(task.getId(), updatedTask.getId()); // ID should not change
    }

    @Test
    public void completeTask_marksTaskAsCompleted() {
        // Arrange
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Study Java");
        Task task = service.createTask(request);

        // Act
        Task completedTask = service.completeTask(task.getId());

        // Assert
        assertTrue(completedTask.isCompleted());
    }

    @Test
    public void deleteTask_removesTask() {
        // Arrange
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Study Java");
        Task task = service.createTask(request);

        // Act
        service.deleteTask(task.getId());

        // Assert
        // If it was deleted, trying to get it again should throw our exception
        assertThrows(IllegalArgumentException.class, () -> {
            service.getTaskById(task.getId());
        });
    }
}