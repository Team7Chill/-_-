package com.example.outsourcing_project.domain.auth.controller;

import com.example.outsourcing_project.domain.auth.controller.dto.ApiResponse;
import com.example.outsourcing_project.domain.auth.controller.dto.LoginRequest;
import com.example.outsourcing_project.domain.auth.service.AuthService;
import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.global.security.Jwt.JwtBlacklistService;
import com.example.outsourcing_project.global.security.Jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginApi(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse login = authService.login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", login.getAccessToken());

        // RefreshToken을 HttpOnly 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtil.substringToken(login.getRefreshToken()));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);  // HTTPS 환경일 경우 true
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int)(jwtUtil.getRefreshTokenExpireTime() / 1000));
        response.addCookie(refreshTokenCookie);

        ApiResponse apiResponse = new ApiResponse(true, "로그인 성공", Instant.now().toString());
        return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String bearerToken) {

        String token = jwtUtil.substringToken(bearerToken);
        String jti = jwtUtil.extractJti(token);  // 토큰에서 jti 추출

        jwtBlacklistService.addBlacklist(jti);

        ApiResponse response = new ApiResponse(true, "로그아웃 성공", Instant.now().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


