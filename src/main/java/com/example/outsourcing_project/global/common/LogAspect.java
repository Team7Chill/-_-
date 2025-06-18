package com.example.outsourcing_project.global.common;

import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.domain.comments.controller.CommentCreateResponseDto;
import com.example.outsourcing_project.domain.log.domain.model.LoggingType;
import com.example.outsourcing_project.domain.log.service.LogSaveDto;
import com.example.outsourcing_project.domain.task.controller.dto.CreateTaskResponseDto;
import com.example.outsourcing_project.global.security.jwt.CustomUserDetails;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LogAspect {

    private final ApplicationEventPublisher publisher;
    private final JwtUtil jwtUtil;

    //서비스 패키지 내의 클래스만 접근
    @Pointcut("execution(* com.example.outsourcing_project.domain..service..*(..)) " +
            "&& !within(com.example.outsourcing_project.domain.log..*)" +
            "&& !within(com.example.outsourcing_project.domain.user..*)")
    public void loggableServiceMethods() {}

    @Around("loggableServiceMethods()")
    public Object saveActivityLog(ProceedingJoinPoint joinPoint) throws Throwable  {

        // HttpServletRequest 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        //비즈니스 로직 수행 시 전달되는 파라미터
        Object[] args = joinPoint.getArgs();

        // 실제 비즈니스 로직 수행
        Object result = joinPoint.proceed();

        String method = request.getMethod();
        if("GET".equals(method)) return result;

        LogSaveDto logSaveDto = new LogSaveDto();
        Long userId = getUserId();
        String uri = request.getRequestURI();

        //LogSaveDto 에 DB 저장 내용 Set
        setLoggingTypeToDto(logSaveDto, args, result, method, uri, userId);

        // 로그 DB 저장 이벤트 발생
        publisher.publishEvent(logSaveDto);

        return result;
    }

    //LogSaveDto 에 DB 저장 내용 Set
    private void setLoggingTypeToDto(LogSaveDto logSaveDto,Object[] args,Object result, String method, String uri, Long userId){
        logSaveDto.setUserId(getUserId());
        logSaveDto.setUri(uri);
        logSaveDto.setMethod(method);

        // 활동 ID 및 활동 유형 설정
        for (LoggingType loggingType : LoggingType.values()) {
            if (loggingType.getMethod().equals(method) && uri.matches(loggingType.getUriPattern())) {
                logSaveDto.setActivityType(loggingType.getType());
                logSaveDto.setContents(loggingType.getContents());
                logSaveDto.setUri(uri);
                logSaveDto.setMethod(method);
                setActivityIdByMethod(logSaveDto, loggingType, method, uri, result, args);
                return;
            }
        }
    }

    //ActivityId는 taks, comment, user 구분하여 Set
    private void setActivityIdByMethod(LogSaveDto logSaveDto, LoggingType loggingType, String method, String uri, Object result, Object[] args){
        //변경 및 삭제 로그
        if(!"POST".equals(method)){
            logSaveDto.setActivityId(extractIdFromUri(uri, loggingType.getIdIndex()));
            return;
        }
        //로그인
        if(result instanceof LoginResponse dto){
            Long userId = getUserIdFromToken(dto.getAccessToken());
            logSaveDto.setUserId(userId);
            logSaveDto.setActivityId(userId);
            return;
        }
        //로그아웃
        if(uri.matches("/api/logout")){
            logSaveDto.setActivityId(getUserId());
            return;
        }
        //댓글 생성
        if (result instanceof CommentCreateResponseDto dto) {
            logSaveDto.setActivityId(dto.getId());
            return;
        }
        //업무 생성
        if (result instanceof CreateTaskResponseDto dto) {
            logSaveDto.setActivityId(dto.getTaskId());
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

    //SecurityContextHolder에서 userId 생성
    private Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails userDetails) {
                return userDetails.getId();
            }
        }
        return null;
    }

    //토큰에서 userId 생성
    private Long getUserIdFromToken(String token){
        String subStringToken = jwtUtil.substringToken(token);
        String userId = jwtUtil.extractUserId(subStringToken);
        return Long.parseLong(userId);
    }
}
