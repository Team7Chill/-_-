package com.example.outsourcing_project.domain.comments.domain.entity;

import com.example.outsourcing_project.domain.task.domain.entity.Task;
import com.example.outsourcing_project.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task taskId;

    @Column(nullable = false)
    private String content;

    public Comments(String content, Task task) {
        this.content = content;
        this.taskId = task;
    }

    public void update(String content) {
        this.content = content;
    }
}
