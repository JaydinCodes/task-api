package com.jaydin.taskapi.repository;
import com.jaydin.taskapi.model.Task;

import java.util.List;
public interface TaskRepository {
    Task save(Task task);
    Task update(Task task);
    Task findById(int id);
    List<Task> findAll();
    boolean deleteById(int id);
}
