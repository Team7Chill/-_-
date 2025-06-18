package com.example.outsourcing_project.domain.user.controller;

import com.example.outsourcing_project.domain.user.controller.dto.RegisterRequestDto;
import com.example.outsourcing_project.domain.user.service.UserService;
import com.example.outsourcing_project.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody  RegisterRequestDto requestDto) {
        userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "화원가입이 완료되었습니다."));
    }
}
