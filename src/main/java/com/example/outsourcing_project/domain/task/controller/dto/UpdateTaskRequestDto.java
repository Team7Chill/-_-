package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.entity.TaskPriority;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateTaskRequestDto {

    private final Long managerId;
    private final String title;
    private final String content;
    private final TaskPriority priority;
    private final LocalDateTime startDate;
    private final LocalDateTime deadLine;

    public UpdateTaskRequestDto(Long managerId, String title, String content, TaskPriority priority, LocalDateTime startDate, LocalDateTime deadLine) {
        this.managerId = managerId;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.startDate = startDate;
        this.deadLine = deadLine;
    }
}
