package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.entity.TaskStatus;
import lombok.Getter;

@Getter
public class UpdateTaskStatusRequestDto {
    private TaskStatus status;
}
