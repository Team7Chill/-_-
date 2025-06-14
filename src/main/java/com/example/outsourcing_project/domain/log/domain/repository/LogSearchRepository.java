package com.example.outsourcing_project.domain.log.domain.repository;

import com.example.outsourcing_project.domain.log.controller.LogRequestDto;
import com.example.outsourcing_project.domain.log.domain.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogSearchRepository {

    Page<Log> findAllFromQueryDsl(LogRequestDto logRequestDto, Pageable pageable);
}
