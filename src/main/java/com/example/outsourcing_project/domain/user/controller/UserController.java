package com.example.outsourcing_project.domain.user.controller;

import com.example.outsourcing_project.domain.user.controller.dto.RegisterRequestDto;
import com.example.outsourcing_project.domain.user.controller.dto.UserResponseDto;
import com.example.outsourcing_project.domain.user.service.UserService;
import com.example.outsourcing_project.global.common.ApiResponse;
import com.example.outsourcing_project.global.enums.UserRoleEnum;
import com.example.outsourcing_project.global.security.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        userService.register(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "화원가입이 완료되었습니다."));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserResponseDto responseDto = UserResponseDto.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .name(userDetails.getName())
                .role(userDetails.getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElse(UserRoleEnum.USER.getRole()))
                .createdAt(null)
                .build();

        return ResponseEntity.ok(ApiResponse.success(responseDto, "회원 정보 조회 성공"));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.delete(userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
