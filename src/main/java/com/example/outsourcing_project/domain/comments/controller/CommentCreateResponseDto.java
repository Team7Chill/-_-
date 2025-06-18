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
    private User userId;
    private Task taskId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public CommentCreateResponseDto(Comments comment) {

    }

    public static CommentCreateResponseDto from(Comments comment) {
        return CommentCreateResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser())
                .taskId(comment.getTask())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}