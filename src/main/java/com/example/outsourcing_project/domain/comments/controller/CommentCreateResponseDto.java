package com.example.outsourcing_project.domain.comments.controller;

import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateResponseDto {
    private Long id;
    private Long userId;
    private Long taskId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public CommentCreateResponseDto(Comments comment) {
        this.content = comment.getContent();
        this.taskId = comment.getTask().getId();
        this.userId = comment.getUser().getId();
    }
}