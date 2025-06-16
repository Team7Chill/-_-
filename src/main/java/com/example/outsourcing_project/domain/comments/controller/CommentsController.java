package com.example.outsourcing_project.domain.comments.controller;


import com.example.outsourcing_project.domain.comments.service.CommentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CreateCommentsResponse> createComment(
            @PathVariable Long taskId,
            @RequestBody @Valid CreateCommentsRequest request) { // TODO: JWT 통한 사용자 인증 추가 필요
        return new ResponseEntity<>(commentsService.createComment(taskId, request.getContent()), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<CreateCommentsResponse>> getAllComments(@PathVariable Long taskId) {
        return new ResponseEntity<>(commentsService.getAllComments(taskId), HttpStatus.OK);
    }

    @GetMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CreateCommentsResponse> getCommentByTask(
            @PathVariable Long taskId,
            @PathVariable Long commentId) {
        return new ResponseEntity<>(commentsService.getCommentByTask(taskId, commentId), HttpStatus.OK);
    }

    @PatchMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<UpdateCommentsResponse> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentsRequest request) {
        commentsService.updateComments(taskId, commentId, request.getComments());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComments(
            @PathVariable Long taskId,
            @PathVariable Long commentId) {
        commentsService.deleteComments(taskId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
