package com.example.outsourcing_project.domain.log.controller;

import com.example.outsourcing_project.domain.log.domain.model.Log;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogResponseDto {
    @Setter
    private Long id;
    private String username;
    private Long activityId;
    private String activityType;
    private String contents;
    private LocalDateTime createdAt;

    public static LogResponseDto toDto(Log log) {
        return new LogResponseDto(
                log.getId(),
                log.getUser() != null ? log.getUser().getUsername() : null,
                log.getActivityId(),
                log.getActivityType(),
                log.getContents(),
                log.getCreatedAt()
        );
    }
}
