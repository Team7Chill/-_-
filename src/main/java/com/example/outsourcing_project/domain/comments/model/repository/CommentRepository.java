package com.example.outsourcing_project.domain.comments.model.repository;

import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import com.example.outsourcing_project.domain.task.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

    Optional<Comments> findByIdAndTask(Long commentId, Task task);

    List<Comments> findByTask(Task task);
}
