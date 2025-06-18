package com.example.outsourcing_project.domain.dashboard.controller.dto;

import com.example.outsourcing_project.domain.task.domain.model.TaskPriority;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import com.example.outsourcing_project.domain.task.domain.model.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TodayTasksResponseDto {
    private Long id; // 태스크 ID
    private String title; // 태스크 제목
    private TaskPriority priority; // 태스크 우선순위
    private TaskStatus status; // 태스크 상태
    private LocalDateTime deadline; // 마감 기한
}
