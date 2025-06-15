package com.example.outsourcing_project.domain.comments.domain.repository;

import com.example.outsourcing_project.domain.comments.domain.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
