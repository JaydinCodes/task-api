package com.jaydin.taskapi.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private int id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime time;

    public Task(int id, String title, String description, Boolean completed, LocalDateTime time){
        this.id = id;
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.description = description;
        this.completed = completed;
        this.time = time;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getTime(){
        return time;
    }


    public void update(
            String title,
            String description) {

        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.description = description;

    }

    public void markCompleted() {
        completed = true;
    }

    @Override
    public String toString(){
        return "Task{id=" + id +
                ", title='" + title + "', " +
                "description='" + description +
                "isCompleted='" + completed +
                "time='" + time +
                "}";
    }

    public void setId(int taskId) {
        this.id = taskId;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public void setCompleted(boolean b) {
        this.completed = b;
    }
}
