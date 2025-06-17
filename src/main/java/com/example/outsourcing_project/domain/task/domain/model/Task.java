package com.example.outsourcing_project.domain.task.domain.model;

import com.example.outsourcing_project.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

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

    // 삭제여부
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // 삭제일
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    // 생성일, 수정일은 BaseTaskTimeEntity에서...

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
}
