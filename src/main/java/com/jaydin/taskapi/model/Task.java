package com.jaydin.taskapi.model;

import java.time.LocalDateTime;

public class Task {

    private int id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime time;

    public Task(int id, String title, String description, Boolean completed, LocalDateTime time){
        this.id = id;
        this.title = requireTitle(title);
        this.description = description == null ? "" : description;
        this.completed = Boolean.TRUE.equals(completed);
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

        this.title = requireTitle(title);
        this.description = description == null ? "" : description;

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

    public void replace(String title, String description, boolean completed) {
        update(title, description);
        this.completed = completed;
    }

    private String requireTitle(String title) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title must not be blank");
        return title;
    }
}
