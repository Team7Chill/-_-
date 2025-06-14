package com.example.outsourcing_project.domain.log.domain.repository;

import com.example.outsourcing_project.domain.log.domain.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long>, LogSearchRepository {
}
