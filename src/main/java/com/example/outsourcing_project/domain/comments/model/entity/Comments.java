package com.example.outsourcing_project.domain.comments.model.entity;

import com.example.outsourcing_project.domain.task.domain.model.Task;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "task_comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false)
    private String content;

    public Comments(String content, Task task) {
        this.content = content;
        this.task = task;
    }

    public void update(String content) {
        this.content = content;
    }
}
