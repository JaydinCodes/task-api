package com.jaydin.taskapi.model;

import java.util.Objects;

public class Task {

    private final int id;
    private String title;
    private String description;
    private boolean completed;

    public Task(int id, String title, String description){
        this.id = id;
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.description = description;
        this.completed = false;

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

    public void update(
            String title,
            String description) {

        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.description = description;

    }

    public void markCompleted() {
        completed = true;
    }

}
