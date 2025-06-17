package com.example.outsourcing_project.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false) // columnDefinition = "BOOLEAN DEFAULT FALSE" 벨리데이션 보류
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    public void setDeletedAt(boolean isDeleted) {
        if (isDeleted) {
            this.deletedAt = LocalDateTime.now();
        } else {
            this.deletedAt = null;
        }
    }

    /**
    삭제일자 사용(처리) 예시입니다.
    task.setDeleted(true);
    taskRepository.save(task);
    **/
}
