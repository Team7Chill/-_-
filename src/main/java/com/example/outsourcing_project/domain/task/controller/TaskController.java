package com.example.outsourcing_project.domain.task.controller;

import com.example.outsourcing_project.domain.task.controller.dto.*;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import com.example.outsourcing_project.domain.task.service.TaskService;
import com.example.outsourcing_project.global.common.ApiResponse;
import com.example.outsourcing_project.global.security.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // 태스크 작성(POST)
    @PostMapping
    public ResponseEntity<ApiResponse<CreateTaskResponseDto>> createTask(
            @Valid @RequestBody CreateTaskRequestDto createTaskRequestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails // jwt 토큰
    ) {

        Long creatorId = userDetails.getId();

        CreateTaskResponseDto responseDto = taskService.createTask(createTaskRequestDto, creatorId);

        return new ResponseEntity<>(
                ApiResponse.success(responseDto, "태스크 작성에 성공했습니다."),
                HttpStatus.CREATED
        );
    }

    // 태스크 전체 조회(GET)
    // 페이징 기능 적용
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDto> taskResponseDtoPage = taskService.getTasks(pageable);

        return new ResponseEntity<>(
                ApiResponse.success(
                        taskResponseDtoPage,
                        "전체 태스크 조회에 성공했습니다."),
                HttpStatus.OK
        );
    }

    // 태스크 검색 조건 추가(제목 검색, 내용 검색, 상태 필터링)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>> searchTask(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) TaskStatus status,
            Pageable pageable
    ) {
        Page<TaskResponseDto> result = taskService.searchTasks(title, content, status, pageable);

        return new ResponseEntity<>(
                ApiResponse.success(
                        result,
                        "태스크 검색에 성공했습니다."),
                HttpStatus.OK
        );
    }

    // 태스크 단건 조회(GET)
    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(@PathVariable Long taskId) {
        TaskResponseDto taskResponseDto = taskService.getTaskById(taskId);


        return new ResponseEntity<>(
                ApiResponse.success(
                        taskResponseDto,
                        "태스크 단건 조회에 성공하였습니다."),
                HttpStatus.OK
        );
    }

    // 태스크 수정(PATCH)
    @PatchMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequestDto updateTaskRequestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();

        TaskResponseDto updatedTask = taskService.updateTask(updateTaskRequestDto, taskId, userId);

        return new ResponseEntity<>(
                ApiResponse.success(
                        updatedTask,
                        "태스크 수정에 성공하였습니다."),
                HttpStatus.OK);
    }

    // 태스크 상태 수정(PATCH)
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<UpdateTaskStatusResponseDto>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskStatusRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();

        UpdateTaskStatusResponseDto responseDto = taskService.updateTaskStatus(taskId, requestDto.getStatus(), userId);

        return new ResponseEntity<>(
                ApiResponse.success(
                        responseDto,
                        "태스크 상태 수정에 성공했습니다."
                ),
                HttpStatus.OK);
    }

    // 태스크 삭제(DELETE)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();

        taskService.softDeleteTask(taskId, userId);

        return new ResponseEntity<>(
                ApiResponse.success(
                        null,
                        "태스크가 삭제되었습니다."
                ),
                HttpStatus.NO_CONTENT);
    }
}
