package com.example.outsourcing_project.domain.comments.controller;

import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateResponseDto {

    private Long id;
    private Long userId;
    private Long taskId;
    private String content;

    public CommentUpdateResponseDto(Comments comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.taskId = comment.getTask().getId();
        this.content = comment.getContent();
    }
}
