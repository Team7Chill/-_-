package com.example.outsourcing_project.domain.log.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
    private String activityType;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String contents;

    //Base Entity 병합 이후 수정
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
