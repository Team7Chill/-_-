package com.example.outsourcing_project.domain.log;

import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.domain.comments.controller.CommentCreateResponseDto;
import com.example.outsourcing_project.domain.log.service.LogSaveDto;
import com.example.outsourcing_project.global.common.LogAspect;
import com.example.outsourcing_project.global.security.jwt.CustomUserDetails;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogAspectTest {

    @InjectMocks
    LogAspect logAspect;

    @Mock
    ApplicationEventPublisher publisher;

    @Mock
    ProceedingJoinPoint joinPoint;

    @Mock
    JwtUtil jwtUtil;

    @Test
    public void POST_요청시_LogSaveDto가_생성되어_이벤트가_발행된다() throws Throwable {
        // given

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(1L);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/tasks/10/comments");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        CommentCreateResponseDto response = new CommentCreateResponseDto();
        ReflectionTestUtils.setField(response, "id", 123L); //필드 강제 주입

        when(joinPoint.proceed()).thenReturn(response); //비즈니스 로직에서 반환

        // when
        Object result = logAspect.saveActivityLog(joinPoint);

        // then
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(publisher).publishEvent(captor.capture());

        Object captured = captor.getValue();
        assertThat(captured).isInstanceOf(LogSaveDto.class);

        LogSaveDto dto = (LogSaveDto) captured;
        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getActivityId()).isEqualTo(123L);
        assertThat(dto.getMethod()).isEqualTo("POST");
        assertThat(dto.getUri()).isEqualTo("/api/tasks/10/comments");
        assertThat(dto.getContents()).isEqualTo("새로운 댓글이 작성되었습니다.");

        assertThat(result).isEqualTo(response);
    }

    @Test
    public void GET_요청시_로그를_저장하지_않는다() throws Throwable {
        // given
        // Request 설정
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/tasks");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Object response = "response";

        when(joinPoint.proceed()).thenReturn(response); //비즈니스 로직에서 반환

        // when
        Object result = logAspect.saveActivityLog(joinPoint);

        // then
        verify(publisher, never()).publishEvent(any());
        assertThat(result).isEqualTo(response);
    }

    @Test
    public void LOGIN_요청시_LogSaveDto가_생성되어_이벤트가_발행된다() throws Throwable {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/login");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        LoginResponse response = new LoginResponse("access", "refresh");

        when(jwtUtil.substringToken("access")).thenReturn("access");
        when(jwtUtil.extractUserId("access")).thenReturn("1");

        when(joinPoint.proceed()).thenReturn(response);

        // when
        Object result = logAspect.saveActivityLog(joinPoint);

        // then
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(publisher).publishEvent(captor.capture());

        Object captured = captor.getValue();
        assertThat(captured).isInstanceOf(LogSaveDto.class);

        LogSaveDto dto = (LogSaveDto) captured;
        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getActivityId()).isEqualTo(1L);
        assertThat(dto.getMethod()).isEqualTo("POST");
        assertThat(dto.getUri()).isEqualTo("/api/login");
        assertThat(dto.getContents()).isEqualTo("로그인 되었습니다.");

        assertThat(result).isEqualTo(response);
    }
    
    

}
