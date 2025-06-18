package com.example.outsourcing_project.domain.auth.service;

import com.example.outsourcing_project.domain.auth.domain.jwtblacklist.JwtBlacklistService;
import com.example.outsourcing_project.domain.auth.domain.refresh.RefreshTokenService;
import com.example.outsourcing_project.domain.auth.controller.dto.LoginRequest;
import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.domain.user.domain.repository.UserRepository;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import com.example.outsourcing_project.global.security.cookie.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final JwtBlacklistService jwtBlacklistService;

    /**
     * 로그인 처리
     * @param request 로그인 요청 DTO
     * @return 액세스 토큰과 리프레시 토큰이 담긴 LoginResponse DTO
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 액세스 토큰 생성: id, role만 담음
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getRole());

        // 리프레시 토큰 생성
        String refreshToken = jwtUtil.createRefreshToken();

        Instant issuedAt = Instant.now();
        Instant expiryDate = issuedAt.plusMillis(jwtUtil.getRefreshTokenExpireTime());

        // 리프레시 토큰 저장
        refreshTokenService.createRefreshToken(user, refreshToken, issuedAt, expiryDate);

        return new LoginResponse(accessToken, refreshToken);
    }

    /**
     * 로그아웃 처리
     * @param bearerToken "Bearer " 포함 액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param response HTTP 응답 (쿠키 삭제용)
     */
    @Transactional
    public void logout(String bearerToken, String refreshToken, HttpServletResponse response) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 액세스 토큰입니다.");
        }

        String accessToken = jwtUtil.substringToken(bearerToken);
        jwtBlacklistService.addBlacklist(accessToken);

        if (refreshToken != null && !refreshToken.isEmpty()) {
            String refreshJti = jwtUtil.extractJti(refreshToken);
            jwtBlacklistService.addBlacklist(refreshJti);

            refreshTokenService.deleteByToken(refreshToken);
        }

        // 쿠키 삭제
        Cookie deleteCookie = CookieUtil.deleteCookie("refreshToken");
        CookieUtil.addCookie(response, deleteCookie);
    }
}
