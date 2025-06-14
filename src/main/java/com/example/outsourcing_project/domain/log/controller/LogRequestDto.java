package com.example.outsourcing_project.domain.log.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogRequestDto {
    private Long userId;
    private String activityType;
    private Long activityId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public LogRequestDto(Long userId, String activityType, Long activityId, LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.activityType = activityType;
        this.activityId = activityId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
