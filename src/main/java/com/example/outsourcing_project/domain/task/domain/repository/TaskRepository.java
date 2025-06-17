package com.example.outsourcing_project.domain.task.domain.repository;

import com.example.outsourcing_project.domain.task.domain.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByIsDeletedFalse(Pageable pageable); // 삭제되지 않은 태스크만 페이징
}
