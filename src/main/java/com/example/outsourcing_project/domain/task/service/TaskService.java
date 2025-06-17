package com.example.outsourcing_project.domain.task.service;

import com.example.outsourcing_project.domain.task.controller.dto.*;
import com.example.outsourcing_project.domain.task.domain.entity.Task;
import com.example.outsourcing_project.domain.task.domain.entity.TaskPriority;
import com.example.outsourcing_project.domain.task.domain.entity.TaskStatus;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import com.example.outsourcing_project.domain.user.domain.User;
import com.example.outsourcing_project.domain.user.domain.UserRepository;
import com.example.outsourcing_project.domain.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 태스크 생성/저장
    @Transactional
    public CreateTaskResponseDto createTask(CreateTaskRequestDto requestDto, Long creatorId) {

        // 유저 조회
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        // 담당자 조회
        User manager = userRepository.findById(requestDto.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("담당자를 찾을 수 없습니다."));


        TaskStatus taskStatus = TaskStatus.TODO;

        // Task 생성
        Task task = new Task(creator, manager, requestDto.getTitle(), requestDto.getContent(), requestDto.getPriority(), taskStatus, requestDto.getStartDate(), requestDto.getDeadLine());

        return new CreateTaskResponseDto(taskRepository.save(task));
    }

    // 태스크 전체 조회
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getTasks(Pageable pageable) {

        return taskRepository.findAllByIsDeletedFalse(pageable).map(TaskResponseDto::new);
    }

    // 태스크 단건 조회
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테스크가 존재하지 않습니다."));
        return new TaskResponseDto(task);
    }

    // 태스크 수정
    @Transactional
    public TaskResponseDto updateTask(UpdateTaskRequestDto requestDto, Long taskId) {
        // 기존 태스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 태스크가 존재하지 않습니다."));

        Long managerId = requestDto.getManagerId();

        // userRepository를 통해 영속 상태인 User 조회
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("담당자를 찾을 수 없습니다."));

        task.setManager(manager);

        // 필드 업데이트
        task.setManager(manager);
        task.setTitle(requestDto.getTitle());
        task.setContent(requestDto.getContent());
        task.setPriority(requestDto.getPriority());
        task.setStartDate(requestDto.getStartDate());
        task.setDeadLine(requestDto.getDeadLine());

        return new TaskResponseDto(taskRepository.save(task));
    }

    // 태스크 상태 수정
    @Transactional
    public UpdateTaskStatusResponseDto updateTaskStatus(Long taskId, TaskStatus updateStatus) {

        // 태스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 태스크가 존재하지 않습니다."));

        task.setStatus(updateStatus);
        Task updatedTask = taskRepository.save(task);

        return new UpdateTaskStatusResponseDto(updatedTask.getId(), updatedTask.getStatus());
    }

    // 태스크 삭제(soft delete)
    @Transactional
    public void softDeleteTask(Long taskId) {
        // 태스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 태스크가 존재하지 않습니다."));

        if (Boolean.TRUE.equals(task.getIsDeleted())) {
            throw new IllegalArgumentException("이미 삭제된 태스크입니다.");
        }

        task.setIsDeleted(true);
        task.setDeletedAt(LocalDateTime.now());

        taskRepository.save(task);
    }

}
