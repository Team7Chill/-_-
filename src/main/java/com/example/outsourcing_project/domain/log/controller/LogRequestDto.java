package com.example.outsourcing_project.domain.log.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class LogRequestDto {
    private Long userId;
    private String activityType;
    private Long activityId;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public LogRequestDto(Long userId, String activityType, Long activityId, LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.activityType = activityType;
        this.activityId = activityId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDateTime() {
        return startDate != null ? startDate.atStartOfDay() : null;
    }

    public LocalDateTime getEndDateTime() {
        return endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
    }
}
