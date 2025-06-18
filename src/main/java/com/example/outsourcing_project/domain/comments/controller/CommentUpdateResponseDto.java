package com.example.outsourcing_project.domain.comments.controller;

import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateResponseDto {

    private Long id;
    private User userId;
    private Task taskId;
    private String content;

    public CommentUpdateResponseDto(Comments comment) {
    }
}
