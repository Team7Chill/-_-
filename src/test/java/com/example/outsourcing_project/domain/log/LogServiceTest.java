package com.example.outsourcing_project.domain.log;

import com.example.outsourcing_project.domain.log.domain.repository.LogRepository;
import com.example.outsourcing_project.domain.log.service.LogService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @InjectMocks
    private LogService logService;

    @Mock
    private LogRepository logRepository;
}
