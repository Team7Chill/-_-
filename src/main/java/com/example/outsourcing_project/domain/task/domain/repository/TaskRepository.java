package com.example.outsourcing_project.domain.task.domain.repository;

import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outsourcing_project.domain.dashboard.controller.dto.TodayTasksResponseDto;


import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    //전체 태스크 수
    @Query("select COUNT(t) from Task t where t.isDeleted = false and t.creator.id = :userId")
    long countAll(@Param("userId") Long userId);

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
            @Param("status") TaskStatus status,
            Pageable pageable
    );

    // 상태별 태스크 수
    @Query("select COUNT(t) from Task t where t.status = :status and t.isDeleted = false and t.creator.id = :userId")
    long countByStatus(@Param("status") TaskStatus status, @Param("userId") Long userId);

    // 마감 기한 초과 태스크 수
    @Query("select COUNT(t) from Task t " +
            "where t.status in ('TODO', 'IN_PROGRESS') " +
            "and t.deadLine < current_timestamp " +
            "and t.isDeleted = false " +
            "and t.creator.id = :userId")
    long countOverdueTasks(@Param("userId") Long userId);

    // 오늘의 태스크(우선순위 정렬)
    @Query("select new com.example.outsourcing_project.domain.dashboard.controller.dto.TodayTasksResponseDto(" +
            "t.id, t.title, t.priority, t.status, t.deadLine) " +
            "from Task t " +
            "where t.manager.id = :userId " +
            "and function('date', t.deadLine) = current_date " +
            "and t.status in ('TODO', 'IN_PROGRESS') " +
            "and t.isDeleted = false " +
            "order by " +
            "case t.priority " +
            "when 'HIGH' then 1 " +
            "when 'MEDIUM' then 2 " +
            "when 'LOW' then 3 " +
            "else 4 end")
    List<TodayTasksResponseDto> findTodayTasks(@Param("userId") Long userId);
}
