package com.example.outsourcing_project.domain.task.service;

import com.example.outsourcing_project.domain.task.controller.dto.CreateTaskRequestDto;
import com.example.outsourcing_project.domain.task.controller.dto.CreateTaskResponseDto;
import com.example.outsourcing_project.domain.task.controller.dto.TaskResponseDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateTaskResponseDto createTask(CreateTaskRequestDto requestDto, Long creatorId) {

//        // 유저 조회
//        User creator = userRepository.findById(creatorId)
//                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));
//
//        // 담당자 조회
//        User manager = userRepository.findById(requestDto.getManager_id())
//                .orElseThrow(() -> new IllegalArgumentException("담당자를 찾을 수 없습니다."));

        // USER 구성이 다 안되서 예시를 적용해서 프로그램 개발
        // 임시 Creator 객체 생성
        User creator = new User(
                creatorId, // id (JPA 관리 X 주의, 실제 저장 안됨)
                "creatorUser",
                "creator@example.com",
                "encoded-password", // 실제 환경에서는 암호화 필요
                "임시 생성자",
                UserRole.USER // 또는 UserRole.ADMIN 등
        );

        // 임시 Manager 객체 생성
        User manager = new User(
                2L,
                "managerUser",
                "manager@example.com",
                "encoded-password",
                "임시 담당자",
                UserRole.USER
        );

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

}
