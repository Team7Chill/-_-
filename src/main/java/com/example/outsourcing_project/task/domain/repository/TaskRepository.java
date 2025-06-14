package com.example.outsourcing_project.task.domain.repository;

import com.example.outsourcing_project.task.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
