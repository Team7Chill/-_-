package com.example.outsourcing_project.domain.dashboard.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DashboardStatsResponseDto {
    private long totalTasks; // 전체 태스크 수
    private long todoTasks; // TODO 태스크 수
    private long inProgressTasks; // IN_PROGRESS 태스크 수
    private long doneTasks; // DONE 태스크 수
    private double completionRate; // 완료율
    private long overdueTasks; // 마감 기한이 지난 태스크 수
}
