package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.entity.TaskStatus;
import lombok.Getter;

// 태스크 상태 수정 요청 DTO
@Getter
public class UpdateTaskStatusRequestDto {
    private TaskStatus status;
}
