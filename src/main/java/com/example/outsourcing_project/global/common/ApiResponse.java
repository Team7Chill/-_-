package com.example.outsourcing_project.global.common;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제거 (ex. data=null일 때 생략 가능)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private T data;
    private Instant timestamp;

    @Builder
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    // 정적 팩토리 메서드
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // 최대한 사용 지양
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "요청이 성공했습니다.");
    }

    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }

}
