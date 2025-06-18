package com.example.outsourcing_project.domain.auth.controller;

import com.example.outsourcing_project.domain.auth.controller.dto.LoginRequest;
import com.example.outsourcing_project.domain.auth.domain.refresh.RefreshToken;
import com.example.outsourcing_project.domain.auth.domain.refresh.RefreshTokenService;
import com.example.outsourcing_project.domain.auth.service.AuthService;
import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.global.common.ApiResponse;
import com.example.outsourcing_project.global.security.cookie.CookieUtil;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginApi(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse login = authService.login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", login.getAccessToken());

        CookieUtil.setRefreshTokenCookie(
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

        Optional<Cookie> refreshCookieOpt = CookieUtil.getCookie(request, "refreshToken");

        if (refreshCookieOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.fail("리프레시 토큰이 존재하지 않습니다."));
        }

        String refreshTokenFromCookie = refreshCookieOpt.get().getValue();

        if (!refreshTokenService.isValidRefreshToken(refreshTokenFromCookie)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.fail("리프레시 토큰이 유효하지 않거나 만료되었습니다."));
        }

        RefreshToken tokenEntity = refreshTokenService.findByToken(refreshTokenFromCookie)
                .orElseThrow(() -> new IllegalStateException("리프레시 토큰을 찾을 수 없습니다."));

        User user = tokenEntity.getUser();

        String newAccessToken = jwtUtil.createAccessToken(user.getId(),user.getRole());

        response.setHeader("Authorization", newAccessToken);

        CookieUtil.setRefreshTokenCookie(
                response,
                refreshTokenFromCookie,
                (int) (jwtUtil.getRefreshTokenExpireTime() / 1000)
        );

        return ResponseEntity.ok(ApiResponse.success(null, "액세스 토큰이 갱신되었습니다."));
    }
}
