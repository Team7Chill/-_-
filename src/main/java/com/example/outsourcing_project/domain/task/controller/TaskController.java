package com.example.outsourcing_project.domain.task.controller;

import com.example.outsourcing_project.domain.task.controller.dto.*;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import com.example.outsourcing_project.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // 태스크 작성(POST)
    @PostMapping
    public ResponseEntity<CustomResponseDto<CreateTaskResponseDto>> createTask(@Valid @RequestBody CreateTaskRequestDto createTaskRequestDto) {

//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    Long creatorId = ((AuthUserDto) authentication.getPrincipal()).getId();

        Long exUserId = 1L;

        CreateTaskResponseDto responseDto = taskService.createTask(createTaskRequestDto, exUserId);

        CustomResponseDto<CreateTaskResponseDto> customResponseDto = new CustomResponseDto<>(
                true,
                "태스크 작성에 성공했습니다.",
                responseDto,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(customResponseDto, HttpStatus.CREATED);
    }

    // 태스크 전체 조회(GET)
    // 페이징 기능 적용
    @GetMapping
    public ResponseEntity<CustomResponseDto<Page<TaskResponseDto>>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDto> taskResponseDtoPage = taskService.getTasks(pageable);

        CustomResponseDto<Page<TaskResponseDto>> responseDto = new CustomResponseDto<>(
                true,
                "전체 태스크 조회에 성공했습니다.",
                taskResponseDtoPage,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 태스크 검색 조건 추가(제목 검색, 내용 검색, 상태 필터링)
    @GetMapping("/search")
    public ResponseEntity<CustomResponseDto<Page<TaskResponseDto>>> searchTask(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false)TaskStatus status,
            Pageable pageable
            ) {
        Page<TaskResponseDto> result = taskService.searchTasks(title, content, status, pageable);

        CustomResponseDto<Page<TaskResponseDto>> responseDto = new CustomResponseDto<>(
                true,
                "태스크 검색에 성공했습니다.",
                result,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 태스크 단건 조회(GET)
    @GetMapping("/{taskId}")
    public ResponseEntity<CustomResponseDto<TaskResponseDto>> getTaskById(@PathVariable Long taskId) {
        TaskResponseDto taskResponseDto = taskService.getTaskById(taskId);

        CustomResponseDto<TaskResponseDto> responseDto = new CustomResponseDto<>(
                true,
                "태스크 조회에 성공하였습니다.",
                taskResponseDto,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 태스크 수정(PATCH)
    @PatchMapping("/{taskId}")
    public ResponseEntity<CustomResponseDto<TaskResponseDto>> updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequestDto updateTaskRequestDto
            ) {

        TaskResponseDto updatedTask = taskService.updateTask(updateTaskRequestDto, taskId);

        CustomResponseDto<TaskResponseDto> responseDto = new CustomResponseDto<>(
                true,
                "태스크 수정에 성공하였습니다.",
                updatedTask,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 태스크 상태 수정(PATCH)
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<CustomResponseDto<UpdateTaskStatusResponseDto>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskStatusRequestDto requestDto
    ) {
        UpdateTaskStatusResponseDto responseDto = taskService.updateTaskStatus(taskId, requestDto.getStatus());

        CustomResponseDto<UpdateTaskStatusResponseDto> customResponseDto = new CustomResponseDto<>(
                true,
                "태스크 상태 수정에 성공했습니다.",
                responseDto,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(customResponseDto, HttpStatus.OK);
    }

    // 태스크 삭제(DELETE)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.softDeleteTask(taskId);
        CustomResponseDto customResponseDto = new CustomResponseDto(
                true,
                "태스크가 삭제되었습니다.",
                null,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(customResponseDto, HttpStatus.NO_CONTENT);
    }
}
