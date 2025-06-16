package com.example.outsourcing_project.domain.log.controller;

import com.example.outsourcing_project.domain.log.domain.model.Log;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogResponseDto {
    private Long id;
    //private String username;
    private Long activityId;
    private String activityType;
    private String contents;
    private LocalDateTime createdAt;

    public LogResponseDto() {
    }

    public LogResponseDto toDto(Log log) {
        this.id = log.getId();
        //this.username = log.getUser().getUsername();
        this.activityId = log.getActivityId();
        this.activityType = log.getActivityType();
        this.contents = log.getContents();
        this.createdAt = log.getCreatedAt();
        return this;
    }
}
