package com.example.outsourcing_project.global.common;

import com.example.outsourcing_project.domain.log.LogSaveDto;
import com.example.outsourcing_project.domain.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogEventHandler {

    private final LogService logService;

    @Async
    @EventListener
    public void addLog(LogSaveDto logSaveDto){
        logService.addLog(logSaveDto);
    }
}
