package com.jaydin.taskapi.dto;

import java.time.LocalDateTime;

public class CreateTaskRequest {

    private String title;
    private String description;
    private Boolean completed;
    private LocalDateTime time;

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

    public LocalDateTime getTime(){
        time =  LocalDateTime.now();
        return time;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
