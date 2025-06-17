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

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks() {

        return taskRepository.findAll().stream()
                .filter(task -> !task.getIsDeleted()) // 삭제되지 않은 태스크만
                .map(TaskResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테스크가 존재하지 않습니다."));
        return new TaskResponseDto(task);
    }

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

    @Transactional
    public UpdateTaskStatusResponseDto updateTaskStatus(Long taskId, TaskStatus updateStatus) {

        // 태스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 태스크가 존재하지 않습니다."));

        task.setStatus(updateStatus);
        Task updatedTask = taskRepository.save(task);

        return new UpdateTaskStatusResponseDto(updatedTask.getId(), updatedTask.getStatus());
    }

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
