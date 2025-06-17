package com.example.outsourcing_project.domain.task.controller.dto;

import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.task.domain.model.TaskPriority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

// 태스크 수정 요청 DTO
@Getter
public class UpdateTaskRequestDto {

    @JsonProperty("manager_id")
    private Long managerId;

    private String title;
    private String content;
    private TaskPriority priority;
    private LocalDateTime startDate;
    private LocalDateTime deadLine;

    public UpdateTaskRequestDto() {};

    public UpdateTaskRequestDto(Task task) {
        this.managerId = task.getManager().getId();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.priority = task.getPriority();
        this.startDate = task.getStartDate();
        this.deadLine = task.getDeadLine();
    }
}
