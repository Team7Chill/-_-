package com.example.outsourcing_project.domain.dashboard.controller.dto;

import com.example.outsourcing_project.domain.task.domain.entity.TaskPriority;
import com.example.outsourcing_project.domain.task.domain.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TodayTasksResponseDto {
    private Long id;
    private String title;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime deadline;
}
