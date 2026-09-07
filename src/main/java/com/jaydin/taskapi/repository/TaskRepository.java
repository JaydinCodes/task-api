package com.jaydin.taskapi.repository;
import com.jaydin.taskapi.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TaskRepository {
    Task save(Task task);
    Task findById(int id);
    List<Task> findAll();
    void deleteById(int id);
    Task replaceTask(Task olderTask, Task newTask);
}
