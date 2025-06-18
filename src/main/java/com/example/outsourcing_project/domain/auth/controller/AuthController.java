package com.example.outsourcing_project.domain.auth.controller;


import com.example.outsourcing_project.domain.auth.controller.dto.LoginRequest;
import com.example.outsourcing_project.domain.auth.service.RefreshTokenService;
import com.example.outsourcing_project.domain.auth.service.AuthService;
import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.global.common.ApiResponse;
import com.example.outsourcing_project.global.security.cookie.CookieUtil;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginApi(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse login = authService.login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", login.getAccessToken());

        cookieUtil.setRefreshTokenCookie(
                response,
                login.getRefreshToken(),
                (int) (jwtUtil.getRefreshTokenExpireTime() / 1000)
        );

        ApiResponse<String> apiResponse = ApiResponse.success(null, "로그인 성공");
        return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        authService.logout(bearerToken, refreshToken, response);

        return ResponseEntity.ok(ApiResponse.success(null, "로그아웃 성공"));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        String newAccessToken = refreshTokenService.refreshAccessTokenFromRequest(request);

        response.setHeader("Authorization", newAccessToken);

        refreshTokenService.setRefreshTokenCookie(response);

        return ResponseEntity.ok(ApiResponse.success(null, "액세스 토큰이 갱신되었습니다."));
    }
}
