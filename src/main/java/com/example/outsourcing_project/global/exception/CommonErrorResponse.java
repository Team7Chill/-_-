package com.example.outsourcing_project.global.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommonErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public CommonErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
