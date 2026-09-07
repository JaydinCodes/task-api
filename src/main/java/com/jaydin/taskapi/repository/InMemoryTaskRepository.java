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
            taskToSave = new Task(nextId, task.getTitle(), task.getDescription(), task.isCompleted() ,task.getTime());
        }
        tasks.add(taskToSave);
        return taskToSave;
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
    public void deleteById(int id) {

        tasks.removeIf(task -> task.getId() == id);

    }

    @Override
    public Task replaceTask(Task olderTask, Task newTask) {
        int index = tasks.indexOf(olderTask);
        if (index < 0) {
            return null;
        }
        newTask.setId(olderTask.getId());
        tasks.set(index, newTask);
        return newTask;
    }

}
