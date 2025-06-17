package com.example.outsourcing_project.domain.log.global;

import com.example.outsourcing_project.domain.log.controller.LogResponseDto;
import com.example.outsourcing_project.domain.log.domain.model.LoggingType;
import com.example.outsourcing_project.domain.log.service.LogSaveDto;
import com.example.outsourcing_project.domain.log.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LogAspect {

    private final LogService logService;

    //서비스 패키지 내의 클래스만 접근
    @Pointcut("execution(* com.example.outsourcing_project.domain..service..*(..))")
    public void loggableServiceMethods() {}

    @AfterReturning(value = "loggableServiceMethods()", returning = "result")
    public void saveActivityLog(Object result) {
        // HttpServletRequest 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // URI & 요청 메서드
        String method = request.getMethod();
        if("GET".equals(method)) return;

        String uri = request.getRequestURI();
        LogSaveDto logSaveDto = new LogSaveDto();

        //LogSaveDto 에 DB 저장 내용 Set
        setLoggingTypeToDto(logSaveDto, result, method, uri);

        // 로그 DB 저장
        logService.addLog(logSaveDto);
    }

    //LogSaveDto 에 DB 저장 내용 Set
    private void setLoggingTypeToDto(LogSaveDto logSaveDto,Object result, String method, String uri){
        logSaveDto.setUri(uri);
        logSaveDto.setMethod(method);

        // 활동 ID 및 활동 유형 설정
        for (LoggingType loggingType : LoggingType.values()) {
            if (loggingType.getMethod().equals(method) && uri.matches(loggingType.getUriPattern())) {
                logSaveDto.setActivityType(loggingType.getType());
                logSaveDto.setContents(loggingType.getContents());
                logSaveDto.setUri(uri);
                logSaveDto.setMethod(method);
                setActivityIdByMethod(logSaveDto, loggingType, method, uri, result);
                return;
            }
        }
    }

    //ActivityId는 taks, comment, user 구분하여 Set
    private void setActivityIdByMethod(LogSaveDto logSaveDto, LoggingType loggingType, String method, String uri, Object result){
        if(!"POST".equals(method)){
            logSaveDto.setActivityId(extractIdFromUri(uri, loggingType.getIdIndex()));
            return;
        }
        if(uri.matches("/api/login") || uri.matches("/api/logout")){
            logSaveDto.setActivityId(1L);
            //logSaveDto.setActivityId(user); 추후 구현
            return;
        }
        //임시 구현
        if (result instanceof LogResponseDto dto) {
            logSaveDto.setActivityId(dto.getId());  // ID만 추출
        }
    }

    //uri 에서 id 추출
    private Long extractIdFromUri(String uri, int index) {
        if(index < 0) return null;

        String[] parts = uri.split("/");
        if (index >= parts.length) return null;

        try {
            return Long.parseLong(parts[index]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
