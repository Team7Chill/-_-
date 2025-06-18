package com.example.outsourcing_project.domain.comments.service;

import com.example.outsourcing_project.domain.comments.controller.CommentCreateResponseDto;
import com.example.outsourcing_project.domain.comments.controller.CommentUpdateResponseDto;
import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import com.example.outsourcing_project.domain.comments.model.repository.CommentRepository;
import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import com.example.outsourcing_project.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public CommentCreateResponseDto createComment(Long taskId, String content) {

        Task task = getTaskOrThrow(taskId);

        Comments savedComments = commentRepository.save(new Comments(content, task));

        return new CommentCreateResponseDto(savedComments);
    }


    public Page<CommentCreateResponseDto> getAllComments(Long taskId, Pageable pageable) {
        Page<Comments> commentPage = commentRepository.findByTaskId(taskId, pageable);
        return commentPage.map(CommentCreateResponseDto::from);
    }

    public CommentCreateResponseDto getCommentByTask(Long taskId, Long commentId) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = getCommentsOrThrow(commentId);

        return new CommentCreateResponseDto(comment);
    }


    @Transactional
    public CommentUpdateResponseDto updateComments(Long taskId, Long commentId, String comments) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = getCommentsOrThrow(commentId);

        comment.update(comments);
        return new CommentUpdateResponseDto(comment);
    }


    @Transactional
    public void deleteComments(Long taskId, Long commentId) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = getCommentsOrThrow(commentId);

        commentRepository.delete(comment);

        // TODO: Soft Delete 처리 메뉴얼
    }

    // Task, Comment 탐색 & 예외처리 공용 메서드
    private Task getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("해당 태스크를 찾을 수 없습니다."));
    }

    private Comments getCommentsOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));
    }
}
