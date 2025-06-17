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
    private Long id;
    private String title;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime deadline;
}
