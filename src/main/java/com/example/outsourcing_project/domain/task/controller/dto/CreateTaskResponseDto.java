package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.task.domain.model.TaskPriority;
import lombok.Getter;

import java.time.LocalDateTime;

// 태스크 생성 응답 DTO
@Getter
public class CreateTaskResponseDto {
    private final Long taskId;
    private final Long managerId;
    private final String managerName;
    private final Long creatorId;
    private final String title;
    private final String content;
    private final TaskPriority priority;
    private final LocalDateTime startDate;
    private final LocalDateTime deadLine;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public CreateTaskResponseDto(Task task) {
        this.taskId = task.getId();
        this.managerId = task.getManager().getId();
        this.managerName = task.getManager().getUsername();
        this.creatorId = task.getCreator().getId();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.priority = task.getPriority();
        this.startDate = task.getStartDate();
        this.deadLine = task.getDeadLine();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
    }
}
