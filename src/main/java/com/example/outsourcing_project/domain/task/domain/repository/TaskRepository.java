package com.example.outsourcing_project.domain.task.domain.repository;
import com.example.outsourcing_project.domain.dashboard.controller.dto.TodayTasksResponseDto;
import com.example.outsourcing_project.domain.task.domain.entity.Task;
import com.example.outsourcing_project.domain.task.domain.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select COUNT(t) from Task t where t.isDeleted = false and t.creator = :userId")
    long countAll(@Param("userId") Long userId);

    @Query("select COUNT(t) from Task t where t.status = :status and t.isDeleted = false and t.creator = :userId")
    long countByStatus(@Param("status") TaskStatus status, @Param("userId") Long userId);

    @Query("select COUNT(t) from Task t " +
            "where t.status in ('TODO', 'IN_PROGRESS') " +
            "and t.deadLine < current_timestamp " +
            "and t.isDeleted = false " +
            "and t.creator = :userId")
    long countOverdueTasks(@Param("userId") Long userId);

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
