package com.example.outsourcing_project.domain.task.controller;

import com.example.outsourcing_project.domain.task.controller.dto.CreateTaskRequestDto;
import com.example.outsourcing_project.domain.task.controller.dto.CreateTaskResponseDto;
import com.example.outsourcing_project.domain.task.controller.dto.CustomResponseDto;
import com.example.outsourcing_project.domain.task.controller.dto.TaskResponseDto;
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

}
