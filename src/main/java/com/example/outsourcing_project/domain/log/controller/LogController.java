package com.example.outsourcing_project.domain.log.controller;

import com.example.outsourcing_project.domain.log.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class LogController {

    private LogService logService;

    @GetMapping("/api/logs")
    public ResponseEntity<Page<LogResponseDto>> getLogs(
            @PageableDefault(page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam Optional<String> activityType,
            @RequestParam Optional<Long> activityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> endDate) {

        //UserId 받는 로직 추가
        Long userId = 1L;


        Page<LogResponseDto> logPage = logService.getLogs(
                pageable,
                LogRequestDto.builder()
                        .userId(userId)
                        .activityType(activityType.orElse(null))
                        .activityId(activityId.orElse(null))
                        .startDate(startDate.orElse(null))
                        .endDate(endDate.orElse(null))
                        .build());
        return ResponseEntity.ok(logPage);
    }
}
