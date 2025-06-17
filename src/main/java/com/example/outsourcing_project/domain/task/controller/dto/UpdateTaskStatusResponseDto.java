package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;

import lombok.Getter;


// 태스크 수정 응답 DTO
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
