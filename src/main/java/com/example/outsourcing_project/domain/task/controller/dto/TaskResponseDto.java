package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.entity.Task;
import com.example.outsourcing_project.domain.task.domain.entity.TaskPriority;
import lombok.Getter;

import java.time.LocalDateTime;

// 태스크 응답 DTO
@Getter
public class TaskResponseDto {

    private final Long taskId;
    private final Long managerId;
    private final Long creatorId;
    private final String title;
    private final String content;
    private final TaskPriority priority;
    private final LocalDateTime startDate;
    private final LocalDateTime deadLine;

    public TaskResponseDto(Task task) {
        this.taskId = task.getId();
        this.managerId = task.getManager().getId();
        this. creatorId = task.getCreator().getId();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.priority = task.getPriority();
        this.startDate = task.getStartDate();
        this.deadLine = task.getDeadLine();
    }

    // 추후 생성일 수정일 추가
}
