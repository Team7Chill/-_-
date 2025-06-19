package com.example.outsourcing_project.domain.comments.controller;

import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class CommentGetResponseDto {

    private Long id;
    private Long userId;
    private Long taskId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public static CommentGetResponseDto from(Comments comment) {
        return CommentGetResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .taskId(comment.getTask().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
