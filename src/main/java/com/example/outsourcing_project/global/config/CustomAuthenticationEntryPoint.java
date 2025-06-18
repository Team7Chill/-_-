package com.example.outsourcing_project.global.config;

import com.example.outsourcing_project.global.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 사용자가 인증이 필요한 API 요청 시,
 * 커스텀 JSON 응답을 반환하는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // JSON 직렬화를 위한 ObjectMapper 주입
    private final ObjectMapper objectMapper;

    /**
     * 인증 실패 시 자동으로 호출되는 메서드
     * @param request  클라이언트 요청 정보
     * @param response 응답 정보 객체 (여기에 JSON으로 직접 작성)
     * @param authException 인증 실패 예외 정보
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        // 1. 실패 메시지를 공통 응답 포맷(ApiResponse)으로 구성
        ApiResponse<Object> errorResponse = ApiResponse.fail("Jwt 토큰이 필요합니다");

        // 2. JSON 문자열로 직렬화
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        // 3. 응답의 Content-Type을 JSON으로 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 4. 응답의 HTTP 상태 코드를 401 (Unauthorized)로 설정
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 5. 한글 깨짐 방지를 위해 인코딩 설정
        response.setCharacterEncoding("UTF-8");

        // 6. JSON 형태로 메시지를 클라이언트에 출력
        response.getWriter().write(responseBody);
    }
}