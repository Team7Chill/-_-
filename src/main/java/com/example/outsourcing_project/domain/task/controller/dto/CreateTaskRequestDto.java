package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.entity.TaskPriority;
import com.example.outsourcing_project.domain.task.domain.entity.TaskStatus;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

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
