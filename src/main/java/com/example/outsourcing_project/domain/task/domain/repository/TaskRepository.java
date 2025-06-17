package com.example.outsourcing_project.domain.task.domain.repository;

import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // 태스크 전체조회 페이징 조회
    Page<Task> findAllByIsDeletedFalse(Pageable pageable); // 삭제되지 않은 태스크만 페이징

    // 태스크 검색(제목, 내용, 상태 필터링)
    @Query("SELECT t From Task t " +
    "WHERE t.isDeleted = false " +
    "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
    "AND (:content IS NULL OR LOWER(t.content) LIKE LOWER(CONCAT('%', :content, '%'))) " +
    "AND (:status IS NULL OR t.status = :status)")
    Page<Task> searchTasks(
            @Param("title") String title,
            @Param("content") String content,
            @Param("status")TaskStatus status,
            Pageable pageable
            );
}
