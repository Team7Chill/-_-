package com.example.outsourcing_project.domain.log.service;

import com.example.outsourcing_project.domain.log.domain.model.LoggingType;
import com.example.outsourcing_project.domain.log.global.LogAspect;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @Mock
    private LogService logService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ServletRequestAttributes requestAttributes;

    @InjectMocks
    private LogAspect logAspect;

    @Test
    public void log_저장에_성공한다(){
        // given
        when(requestAttributes.getRequest()).thenReturn(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);
        when(request.getRequestURI()).thenReturn("/api/tasks/1");
        when(request.getMethod()).thenReturn("PATCH");

        // when
        logAspect.saveActivityLog(null);

        // then
        ArgumentCaptor<LogSaveDto> captor = ArgumentCaptor.forClass(LogSaveDto.class);
        verify(logService, times(1)).addLog(captor.capture());

        LogSaveDto captured = captor.getValue();

        // 직접 값 비교
        assertNotNull(captured);
        assertEquals("/api/tasks/1", captured.getUri());
        assertEquals("PATCH", captured.getMethod());
        assertEquals(1L, captured.getActivityId());
        assertEquals(LoggingType.TASK_UPDATED.getType(), captured.getActivityType());
        assertEquals(LoggingType.TASK_UPDATED.getContents(), captured.getContents());
    }
}
