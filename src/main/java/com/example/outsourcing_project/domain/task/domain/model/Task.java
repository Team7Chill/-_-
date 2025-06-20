package com.example.outsourcing_project.domain.task.domain.model;

import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    // 태스크 id (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    // 생성자
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    // 담당자
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    // 제목
    @Column(nullable = false)
    private String title;

    // 설명
    @Column(nullable = true)
    private String content;

    // 우선순위
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    // 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    // 시작일
    @Column(name = "startdate")
    private LocalDateTime startDate;

    // 마감일
    @Column(name = "deadline")
    private LocalDateTime deadLine;


    // 생성일, 수정일은 BaseEntity에서...

    public Task() {}

    public Task(User creator, User manager, String title, String content, TaskPriority priority, TaskStatus status, LocalDateTime startDate, LocalDateTime deadLine) {
        this.creator = creator;
        this.manager = manager;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.status = status;
        this.startDate = startDate;
        this.deadLine = deadLine;
    }

    // 전체 정보 업데이트 메서드
    public void update(User manager, String title, String content, TaskPriority priority, LocalDateTime startDate, LocalDateTime deadLine) {
        this.manager = manager;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.startDate = startDate;
        this.deadLine = deadLine;
    }

    // 상태 업데이트
    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

}
