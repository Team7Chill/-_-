package com.example.outsourcing_project.domain.comments.controller;


import com.example.outsourcing_project.domain.comments.service.CommentService;
import com.example.outsourcing_project.global.common.ApiResponse;
import com.example.outsourcing_project.global.security.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> createComment(
            @PathVariable Long taskId,
            @RequestBody @Valid CommentCreateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // TODO: JWT 통한 사용자 인증 추가 필요


        Long userId = userDetails.getId();

        CommentCreateResponseDto comment = commentService.createComment(taskId, userId, request.getContent());
        
        return ResponseEntity.ok(ApiResponse.success(comment,"댓글 작성 성공"));
    }


    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<Page<CommentCreateResponseDto>>> getAllComments(
            @PathVariable Long taskId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentCreateResponseDto> commentPage = commentService.getAllComments(taskId, pageable);
        String message = String.format("%d 태스크의 댓글 전체 조회", taskId);
        return ResponseEntity.ok(ApiResponse.success(commentPage, message));
    }


    @GetMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> getCommentByTask(
            @PathVariable Long taskId,
            @PathVariable Long commentId) {
        CommentCreateResponseDto commentByTask = commentService.getCommentByTask(taskId, commentId);
        String message = String.format("%d 태스크의 댓글 단건 조회", taskId);
        return ResponseEntity.ok(ApiResponse.success(commentByTask, message));
    }


    @PatchMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponseDto>> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();

        CommentUpdateResponseDto updateCommentsResponse = commentService.updateComments(taskId, userId, commentId, request.getComments());
        String message = String.format("%d 태스크의 댓글 수정", taskId);
        return ResponseEntity.ok(ApiResponse.success(updateCommentsResponse, message));
    }


    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComments(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();

        commentService.deleteComments(taskId, userId, commentId);
        String message = String.format("%d 태스크의 댓글 삭제", taskId);
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }
}
