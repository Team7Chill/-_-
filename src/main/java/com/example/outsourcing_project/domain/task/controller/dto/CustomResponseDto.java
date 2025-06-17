package com.example.outsourcing_project.domain.task.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

// 팀에서 정한 응답 규칙 DTO
@Getter
@RequiredArgsConstructor
public class CustomResponseDto<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final ZonedDateTime timestamp;

}
