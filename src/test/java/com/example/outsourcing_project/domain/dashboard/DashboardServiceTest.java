package com.example.outsourcing_project.domain.dashboard;

import com.example.outsourcing_project.domain.dashboard.controller.dto.DashboardStatsResponseDto;
import com.example.outsourcing_project.domain.dashboard.controller.dto.TodayTasksResponseDto;
import com.example.outsourcing_project.domain.dashboard.service.DashboardService;
import com.example.outsourcing_project.domain.task.domain.model.TaskPriority;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {
    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private TaskRepository taskRepository;
    private Long userId;

    @Test
    void 대시보드_통계() {
        userId = 1L;

        // given
        when(taskRepository.countAll(userId)).thenReturn(10L);
        when(taskRepository.countByStatus(TaskStatus.TODO, userId)).thenReturn(4L);
        when(taskRepository.countByStatus(TaskStatus.IN_PROGRESS, userId)).thenReturn(3L);
        when(taskRepository.countByStatus(TaskStatus.DONE, userId)).thenReturn(3L);
        when(taskRepository.countOverdueTasks(userId)).thenReturn(2L);

        // when
        DashboardStatsResponseDto actualResult = dashboardService.getDashboardStats(userId);

        // then
        DashboardStatsResponseDto expectedResult = DashboardStatsResponseDto.builder()
                .totalTasks(10L)
                .todoTasks(4L)
                .inProgressTasks(3L)
                .doneTasks(3L)
                .completionRate(30.0)
                .overdueTasks(2L)
                .build();

        assertThat(actualResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);

        verify(taskRepository, times(1)).countAll(userId);
        verify(taskRepository, times(1)).countByStatus(TaskStatus.TODO, userId);
        verify(taskRepository, times(1)).countByStatus(TaskStatus.IN_PROGRESS, userId);
        verify(taskRepository, times(1)).countByStatus(TaskStatus.DONE, userId);
        verify(taskRepository, times(1)).countOverdueTasks(userId);
    }

    @Test
    void 오늘의_태스크(){
        // given
        List<TodayTasksResponseDto> todayTasks = List.of(
                new TodayTasksResponseDto(1L, "제목1", TaskPriority.HIGH, TaskStatus.TODO, LocalDateTime.now()),
                new TodayTasksResponseDto(2L, "제목2", TaskPriority.MEDIUM, TaskStatus.IN_PROGRESS, LocalDateTime.now())
        );
        when(taskRepository.findTodayTasks(userId)).thenReturn(todayTasks);

        // when
        List<TodayTasksResponseDto> actualResult = dashboardService.getTodayTasks(userId);

        // then
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult).usingRecursiveComparison().isEqualTo(todayTasks);
        verify(taskRepository, times(1)).findTodayTasks(userId);
    }
}
