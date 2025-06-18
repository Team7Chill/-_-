package com.example.outsourcing_project.domain.dashboard.service;

import com.example.outsourcing_project.domain.dashboard.controller.dto.DashboardStatsResponseDto;
import com.example.outsourcing_project.domain.dashboard.controller.dto.TodayTasksResponseDto;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskRepository taskRepository;

    // 전체 통계 조회 로직
    public DashboardStatsResponseDto getDashboardStats(Long userId) {
        long total = taskRepository.countAll(userId);
        long todo = taskRepository.countByStatus(TaskStatus.TODO, userId);
        long inProgress = taskRepository.countByStatus(TaskStatus.IN_PROGRESS, userId);
        long done = taskRepository.countByStatus(TaskStatus.DONE, userId);
        long overdue = taskRepository.countOverdueTasks(userId);

        double rate = Math.round((done * 10000.0 / total)) / 100.0;

        return DashboardStatsResponseDto.builder()
                .totalTasks(total)
                .todoTasks(todo)
                .inProgressTasks(inProgress)
                .doneTasks(done)
                .completionRate(rate)
                .overdueTasks(overdue)
                .build();
    }

    // 오늘의 태스크 요약 로직
    public List<TodayTasksResponseDto> getTodayTasks(Long userId) {
        return taskRepository.findTodayTasks(userId);
    }
}
