package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.model.TaskPriority;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

// 태스크 생성 요청 DTO
@Getter
public class CreateTaskRequestDto {

    @JsonProperty("manager_id")
    private final Long managerId;

    private final String title;

    private final String content;

    private final TaskPriority priority;


    private final TaskStatus status;


    private final LocalDateTime startDate;


    private final LocalDateTime deadLine;

    public CreateTaskRequestDto(Long managerId, String title, String content, TaskPriority priority, TaskStatus status, LocalDateTime startDate, LocalDateTime deadLine) {
        this.managerId = managerId;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.status = status;
        this.startDate = startDate;
        this.deadLine = deadLine;
    }
}
