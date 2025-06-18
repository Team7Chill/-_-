package com.example.outsourcing_project.domain.log.domain.model;

import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Log extends BaseEntity {
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
}
