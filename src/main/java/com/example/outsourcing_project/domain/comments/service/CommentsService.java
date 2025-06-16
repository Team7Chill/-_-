package com.example.outsourcing_project.domain.comments.service;

import com.example.outsourcing_project.domain.comments.controller.CreateCommentsResponse;
import com.example.outsourcing_project.domain.comments.controller.UpdateCommentsResponse;
import com.example.outsourcing_project.domain.comments.domain.entity.Comments;
import com.example.outsourcing_project.domain.comments.domain.repository.CommentsRepository;
import com.example.outsourcing_project.domain.task.domain.entity.Task;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import com.example.outsourcing_project.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final TaskRepository taskRepository;

    public CreateCommentsResponse createComment(Long taskId, String content) {

        Task task = getTaskOrThrow(taskId);

        Comments savedComments = commentsRepository.save(new Comments(content, task));

        return new CreateCommentsResponse(savedComments);
    }


    public List<CreateCommentsResponse> getAllComments(Long taskId) {
        Task task = getTaskOrThrow(taskId);

        List<Comments> commentsList = commentsRepository.findByTask(task);

        return commentsList.stream()
                .map(CreateCommentsResponse::new)
                .toList();
    }

    public CreateCommentsResponse getCommentByTask(Long taskId, Long commentId) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = commentsRepository.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("해당 태스크에 댓글이 존재하지 않습니다."));

        return new CreateCommentsResponse(comment);
    }

    public UpdateCommentsResponse updateComments(Long taskId, Long commentId, String comments) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = commentsRepository.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("해당 태스크에 댓글이 존재하지 않습니다."));

        comment.update(comments);
        return new UpdateCommentsResponse(comment);
    }


    public void deleteComments(Long taskId, Long commentId) {
        Task task = getTaskOrThrow(taskId);

        Comments comment = commentsRepository.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("해당 태스크에 댓글이 존재하지 않습니다."));

        commentsRepository.delete(comment);
    }

    // 태스크 탐색 & 예외처리 공용 메서드
    private Task getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("해당 태스크를 찾을 수 없습니다."));
    }
}
