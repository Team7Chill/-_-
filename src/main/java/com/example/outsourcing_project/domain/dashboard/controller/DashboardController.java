package com.example.outsourcing_project.domain.dashboard.controller;

import com.example.outsourcing_project.domain.dashboard.controller.dto.DashboardStatsResponseDto;
import com.example.outsourcing_project.domain.dashboard.controller.dto.TodayTasksResponseDto;
import com.example.outsourcing_project.domain.dashboard.service.DashboardService;
import com.example.outsourcing_project.global.common.ApiResponse;
import com.example.outsourcing_project.global.exception.UnauthorizedException;
import com.example.outsourcing_project.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboards")
public class DashboardController {
    private final DashboardService dashboardService;

    // 전체 통계 조회
    @GetMapping
    public ApiResponse<DashboardStatsResponseDto> getDashboardStats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요한 기능입니다.");
        }
        Long userId = userDetails.getId();
        DashboardStatsResponseDto stats = dashboardService.getDashboardStats(userId);
        return ApiResponse.success(stats, "조회 성공");
    }

    // 오늘의 태스크 요약
    @GetMapping("/summary")
    public ApiResponse<List<TodayTasksResponseDto>> getTodayTasks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요한 기능입니다.");
        }
        Long userId = userDetails.getId();
        List<TodayTasksResponseDto> tasks = dashboardService.getTodayTasks(userId);
        return ApiResponse.success(tasks, "조회 성공");
    }
}
