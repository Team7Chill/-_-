package com.example.outsourcing_project.domain.auth.controller;

import com.example.outsourcing_project.domain.auth.controller.dto.ApiResponse;
import com.example.outsourcing_project.domain.auth.controller.dto.LoginRequest;
import com.example.outsourcing_project.domain.auth.service.AuthService;
import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.global.security.Jwt.JwtBlacklistService;
import com.example.outsourcing_project.global.security.Jwt.JwtUtil;
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
    public ResponseEntity<ApiResponse> loginApi(@Valid @RequestBody LoginRequest request) {

        LoginResponse login = authService.Login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + login.getBearerToken());

        ApiResponse response = new ApiResponse(true, "로그인 성공", Instant.now().toString());

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String bearerToken) {

        String token = jwtUtil.substringToken(bearerToken);

        jwtBlacklistService.addBlacklist(token);

        ApiResponse response = new ApiResponse(true, "로그아웃 성공", Instant.now().toString());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}


