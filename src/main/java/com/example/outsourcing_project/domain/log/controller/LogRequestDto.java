package com.example.outsourcing_project.domain.log.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class LogRequestDto {
    private String activityType;
    private Long activityId;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public LogRequestDto(String activityType, Long activityId, LocalDate startDate, LocalDate endDate) {
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

    public static LogRequestDto toBuild(Optional<String> activityType,
                                           Optional<Long> activityId,
                                           Optional<LocalDate> startDate,
                                           Optional<LocalDate> endDate) {
        return LogRequestDto.builder()
                .activityType(activityType.orElse(null))
                .activityId(activityId.orElse(null))
                .startDate(startDate.orElse(null))
                .endDate(endDate.orElse(null))
                .build();
    }
}
