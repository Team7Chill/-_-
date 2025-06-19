package com.example.outsourcing_project.domain.task.service;

import com.example.outsourcing_project.domain.task.controller.dto.*;
import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.task.domain.model.TaskStatus;
import com.example.outsourcing_project.domain.task.domain.repository.TaskRepository;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.domain.user.domain.repository.UserRepository;
import com.example.outsourcing_project.global.exception.NotFoundException;
import com.example.outsourcing_project.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new NotFoundException("작성자를 찾을 수 없습니다."));

        // 담당자 조회
        User manager = userRepository.findById(requestDto.getManagerId())
                .orElseThrow(() -> new NotFoundException("담당자를 찾을 수 없습니다."));


        TaskStatus taskStatus = TaskStatus.TODO; //초기 상태 설정

        // Task 생성
        Task task = new Task(creator, manager, requestDto.getTitle(), requestDto.getContent(), requestDto.getPriority(), taskStatus, requestDto.getStartDate(), requestDto.getDeadLine());

        return new CreateTaskResponseDto(taskRepository.save(task));
    }

    // 태스크 전체 조회
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getTasks(Pageable pageable) {

        return taskRepository.findAllByIsDeletedFalse(pageable).map(TaskResponseDto::new);
    }

    // 태스크 검색 조건 추가 (제목, 내용, 상태필터링)
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> searchTasks(String title, String content, TaskStatus status, Pageable pageable) {
        Page<Task> tasks = taskRepository.searchTasks(title, content, status, pageable);

        return tasks.map(TaskResponseDto::new);
    }

    // 태스크 단건 조회
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("해당 ID의 테스크가 존재하지 않습니다."));
        return new TaskResponseDto(task);
    }

    // 태스크 수정
    @Transactional
    public TaskResponseDto updateTask(UpdateTaskRequestDto requestDto, Long taskId, Long userId) {

        // 기존 태스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("해당 태스크가 존재하지 않습니다."));

        Long managerId = requestDto.getManagerId();

        if (!task.getCreator().getId().equals(userId) && !managerId.equals(userId)) {
            throw new UnauthorizedException("해당 작업을 수행할 권한이 없습니다");
        }

        // userRepository를 통해 User 조회
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("담당자를 찾을 수 없습니다."));

        // task 업데이트
        task.update(manager,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getPriority(),
                requestDto.getStartDate(),
                requestDto.getDeadLine());

        return new TaskResponseDto(task);
    }

    // 태스크 상태 수정
    @Transactional
    public UpdateTaskStatusResponseDto updateTaskStatus(Long taskId, TaskStatus updateStatus, Long userId) {

        // 태스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("해당 태스크가 존재하지 않습니다."));

        if (!task.getCreator().getId().equals(userId) && !task.getManager().getId().equals(userId)) {
            throw new UnauthorizedException("해당 작업을 수행할 권한이 없습니다");
        }

        task.updateStatus(updateStatus);

        return new UpdateTaskStatusResponseDto(task.getId(), task.getStatus());
    }

    // 태스크 삭제(soft delete)
    @Transactional
    public void softDeleteTask(Long taskId, Long userId) {
        // 태스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("해당 태스크가 존재하지 않습니다."));

        if (!task.getCreator().getId().equals(userId) && !task.getManager().getId().equals(userId)) {
            throw new UnauthorizedException("해당 작업을 수행할 권한이 없습니다");
        }

        task.delete();
    }

}
