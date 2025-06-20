package com.example.outsourcing_project.domain.comments.model.repository;

import com.example.outsourcing_project.domain.comments.model.entity.Comments;
import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

    Optional<Comments> findByIdAndTask(Long commentId, Task task);

    List<Comments> findByTask(Task task);

    Long user(User user);

    Page<Comments> findByTaskId(Long taskId, Pageable pageable);

}
