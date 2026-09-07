package com.jaydin.taskapi.repository;


import com.jaydin.taskapi.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final List<Task> tasks;
    private int nextId;

    public InMemoryTaskRepository(){
        tasks = new ArrayList<>();
        this.nextId = 1;
    }

    @Override
    public Task save(Task task) {
        Task taskToSave = task;
        if (task.getId() == 0){
            taskToSave = new Task(nextId++, task.getTitle(), task.getDescription(), task.isCompleted() ,task.getTime());
        }
        tasks.add(taskToSave);
        return taskToSave;
    }

    @Override
    public Task update(Task task) {
        int index = tasks.indexOf(task);
        return index < 0 ? null : tasks.set(index, task);
    }

    @Override
    public Task findById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public boolean deleteById(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

}
