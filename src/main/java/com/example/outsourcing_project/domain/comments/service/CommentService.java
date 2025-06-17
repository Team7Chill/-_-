package com.example.outsourcing_project.domain.comments.service;

import com.example.outsourcing_project.domain.comments.controller.CommentCreateResponseDto;
import com.example.outsourcing_project.domain.comments.controller.CommentUpdateResponseDto;
import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import com.example.outsourcing_project.domain.comments.model.repository.CommentRepository;
import com.example.outsourcing_project.domain.task.domain.entity.Task;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import com.example.outsourcing_project.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentCreateResponseDto createComment(Long taskId, String content) {

        Task task = getTaskOrThrow(taskId);

        Comments savedComments = commentRepository.save(new Comments(content, task));

        return new CommentCreateResponseDto(savedComments);
    }


    public List<CommentCreateResponseDto> getAllComments(Long taskId) {
        Task task = getTaskOrThrow(taskId);

        List<Comments> commentsList = commentRepository.findByTask(task);

        return commentsList.stream()
                .map(CommentCreateResponseDto::new)
                .toList();
    }

    public CommentCreateResponseDto getCommentByTask(Long taskId, Long commentId) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = commentRepository.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("해당 태스크에 댓글이 존재하지 않습니다."));

        return new CommentCreateResponseDto(comment);
    }

    public CommentUpdateResponseDto updateComments(Long taskId, Long commentId, String comments) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = commentRepository.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("해당 태스크에 댓글이 존재하지 않습니다."));

        comment.update(comments);
        return new CommentUpdateResponseDto(comment);
    }


    public void deleteComments(Long taskId, Long commentId) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = commentRepository.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("해당 태스크에 댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);
    }

    // 태스크 탐색 & 예외처리 공용 메서드
    private Task getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("해당 태스크를 찾을 수 없습니다."));
    }
}
