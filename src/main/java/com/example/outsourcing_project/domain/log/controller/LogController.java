package com.example.outsourcing_project.domain.log.controller;

import com.example.outsourcing_project.domain.log.service.LogService;
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
    public ResponseEntity<Page<LogResponseDto>> getLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Optional<String> activityType,
            @RequestParam Optional<Long> activityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction //ASC, DESC
    ) {

        //UserId 받는 로직 추가
        Long userId = 1L;


        Page<LogResponseDto> logPage = logService.getLogs(
                PageRequest.of(
                        Math.max(page - 1, 0), //음수 및 0 방지
                        size,
                        Sort.Direction.fromString(direction),
                        sort
                ),
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
