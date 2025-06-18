package com.example.outsourcing_project.domain.log;

import com.example.outsourcing_project.global.common.LogAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class LogControllerTest {

    @InjectMocks
    LogAspect logAspect;

    @Mock
    ApplicationEventPublisher publisher;

    @Mock
    ProceedingJoinPoint joinPoint;


}
