package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.entity.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateTaskStatusResponseDto {

    private final Long taskId;
    private final TaskStatus status;
    //    private final LocalDateTime updatedAt;

    public UpdateTaskStatusResponseDto(Long taskId, TaskStatus status) {
        this.taskId = taskId;
        this.status = status;
    }

}
