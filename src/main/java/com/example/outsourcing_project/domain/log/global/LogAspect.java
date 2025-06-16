package com.example.outsourcing_project.domain.log.global;

import com.example.outsourcing_project.domain.log.controller.LogResponseDto;
import com.example.outsourcing_project.domain.log.domain.model.ActivityType;
import com.example.outsourcing_project.domain.log.service.LogSaveDto;
import com.example.outsourcing_project.domain.log.service.LogService;
import com.example.outsourcing_project.domain.user.domain.UserRepository;
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
    private final UserRepository userRepository;

    @Pointcut("execution(* com.example.outsourcing_project.domain..service..*(..))")
    public void testCodeMethod() {}

    @AfterReturning(value = "testCodeMethod()", returning = "result")
    public void logActivityInfo(Object result) {
        // HttpServletRequest 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // URI & 요청 메서드
        String method = request.getMethod();
        if(!"GET".equals(method)) return;

        String uri = request.getRequestURI();
        LogSaveDto logSaveDto = new LogSaveDto();
        logSaveDto.setUri(uri);
        logSaveDto.setMethod(method);

        // 실제 유저 정보를 가져와야 합니다. -> 임시 구현. 추후 UserService 에서 로직 수행하도록 수정
//        User user = new User();
//        user.setUsername("john_doe");
//        user.setEmail("john.doe@example.com");
//        user.setPassword("password123");
//        user.setName("John Doe");
//        user.setUserRole(UserRole.USER);  // Enum 예시: USER, ADMIN 등
//        user.setId(userRepository.save(user).getId());
//        logSaveDto.setUser(user);

        // 활동 ID 및 활동 유형 설정
        if (!"POST".equals(method)) {
            if (uri.matches("/api/tasks/\\d+")) { // 선택적 status 처리
                logSaveDto.setActivityId(extractIdFromUrl(uri, 3));
                if ("PATCH".equals(method)) {
                    logSaveDto.setActivityType(ActivityType.TASK_UPDATED.getType());
                    logSaveDto.setContents(ActivityType.TASK_UPDATED.getContents());
                }
                if ("DELETE".equals(method)) {
                    logSaveDto.setActivityType(ActivityType.TASK_DELETED.getType());
                    logSaveDto.setContents(ActivityType.TASK_DELETED.getContents());
                }
            }
            if(uri.matches("/api/tasks/\\d+/status/")){
                logSaveDto.setActivityId(extractIdFromUrl(uri, 3));
                logSaveDto.setActivityType(ActivityType.TASK_STATUS_CHANGED.getType());
                logSaveDto.setContents(ActivityType.TASK_STATUS_CHANGED.getContents());
            }
            if (uri.matches("/api/tasks/\\d+/comments/\\d+")) {
                logSaveDto.setActivityId(extractIdFromUrl(uri, 5));
                if ("PATCH".equals(method)) {
                    logSaveDto.setActivityType(ActivityType.COMMENT_UPDATED.getType());
                    logSaveDto.setContents(ActivityType.COMMENT_UPDATED.getContents());
                }
                if ("DELETE".equals(method)) {
                    logSaveDto.setActivityType(ActivityType.COMMENT_DELETED.getType());
                    logSaveDto.setContents(ActivityType.COMMENT_DELETED.getContents());
                }
            }
        } else {
            if (uri.matches("/api/login")) {
                logSaveDto.setActivityId(1L); //임시데이터.
                logSaveDto.setActivityType(ActivityType.USER_LOGGED_IN.getType());
                logSaveDto.setContents(ActivityType.USER_LOGGED_IN.getContents());
            }
            if (uri.matches("/api/logout")) {
                logSaveDto.setActivityId(1L); //임시데이터.
                logSaveDto.setActivityType(ActivityType.USER_LOGGED_OUT.getType());
                logSaveDto.setContents(ActivityType.USER_LOGGED_OUT.getContents());
            }
            if (uri.matches("/api/tasks")) {
                //Test 용 dto. 변경예정
                if (result instanceof LogResponseDto dto) {
                    logSaveDto.setActivityId(dto.getId());  // ID만 추출
                }
                logSaveDto.setActivityType(ActivityType.TASK_CREATED.getType());
                logSaveDto.setContents(ActivityType.TASK_CREATED.getContents());
            }
            if (uri.matches("/api/tasks/\\d+/comments")) {
                //Test 용 dto. 변경예정
                if (result instanceof LogResponseDto dto) {
                    logSaveDto.setActivityId(dto.getId());   // ID만 추출
                }
                logSaveDto.setActivityType(ActivityType.COMMENT_CREATED.getType());
                logSaveDto.setContents(ActivityType.COMMENT_CREATED.getContents());
            }
        }

        // 로그 DB 저장
        logService.addLog(logSaveDto);
    }


    private Long extractIdFromUrl(String uri, int arrNum) {
        String[] parts = uri.split("/");

        if (parts.length > arrNum) {
            try {
                return Long.parseLong(parts[arrNum]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
