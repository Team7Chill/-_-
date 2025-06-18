package com.example.outsourcing_project.domain.log.controller;

import com.example.outsourcing_project.domain.log.LogService;
import com.example.outsourcing_project.global.common.ApiResponse;
import com.example.outsourcing_project.global.common.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class LogController {

    private LogService logService;

    @GetMapping("/api/logs")
    public ResponseEntity<ApiResponse<PageResponse<LogResponseDto>>> getLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Optional<String> activityType,
            @RequestParam Optional<Long> activityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction //ASC, DESC
    ) {

        LogRequestDto requestDto = LogRequestDto.toBuild(activityType, activityId, startDate, endDate);

        Page<LogResponseDto> logPage = logService.getLogs(
                PageRequest.of(
                        Math.max(page - 1, 0), //음수 및 0 방지
                        size,
                        Sort.Direction.fromString(direction),
                        sort
                ),
                requestDto);

        PageResponse<LogResponseDto> response = PageResponse.toResponse(logPage);

        return ResponseEntity.ok(ApiResponse.success(response, "로그 조회 성공"));
    }
}
