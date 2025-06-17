package com.example.outsourcing_project.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
            BadRequestException.class, HttpStatus.BAD_REQUEST,
            UnauthorizedException.class, HttpStatus.UNAUTHORIZED,
            ForbiddenException.class, HttpStatus.FORBIDDEN,
            NotFoundException.class, HttpStatus.NOT_FOUND
    );

    @ExceptionHandler({
            BadRequestException.class,
            UnauthorizedException.class,
            ForbiddenException.class,
            NotFoundException.class
    })
    public ResponseEntity<CommonErrorResponse> handleCustomExceptions(HttpServletRequest request, RuntimeException ex) {
        HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);

        CommonErrorResponse response = new CommonErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, status);
    }
}
