package com.example.outsourcing_project.domain.task.controller;

import com.example.outsourcing_project.domain.task.controller.dto.*;
import com.example.outsourcing_project.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

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

    @GetMapping
    public ResponseEntity<CustomResponseDto<List<TaskResponseDto>>> getAllTasks() {
        List<TaskResponseDto> taskList = taskService.getAllTasks();

        CustomResponseDto<List<TaskResponseDto>> responseDto = new CustomResponseDto<>(
                true,
                "전체 태스크 조회에 성공했습니다.",
                taskList,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

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

    // 1. 과제 SpringSecurity 관련 문제 부분 관련 정리해서 내일 스크럼시간에 발표하기
    // 2. 페이지네이션
    // 3. 기존 git 토큰과 ssh관련 부분 가이드북 작성과제는 목요일까지

}
