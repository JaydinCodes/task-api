package com.jaydin.taskapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskRequest {

    @NotBlank(message = "title must not be blank")
    @Size(max = 50, message = "title must be at most 50 characters")
    private String title;
    private String description;
    private Boolean completed;

    public CreateTaskRequest() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isCompleted(){
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
