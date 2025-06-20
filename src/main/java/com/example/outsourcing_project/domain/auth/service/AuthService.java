package com.example.outsourcing_project.domain.auth.service;

import com.example.outsourcing_project.domain.auth.controller.dto.LoginRequest;
import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.domain.user.domain.repository.UserRepository;
import com.example.outsourcing_project.global.common.Loggable;
import com.example.outsourcing_project.global.exception.UnauthorizedException;
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
    private final CookieUtil cookieUtil;
    private final RefreshTokenService refreshTokenService;
    private final JwtBlacklistService jwtBlacklistService;

    @Loggable
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("가입되지 않은 유저입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getRole());

        String refreshToken = jwtUtil.createRefreshToken();
        Instant issuedAt = Instant.now();
        Instant expiryDate = issuedAt.plusMillis(jwtUtil.getRefreshTokenExpireTime());
        refreshTokenService.createRefreshToken(user, refreshToken, issuedAt, expiryDate);

        return new LoginResponse(accessToken, refreshToken);
    }


    @Loggable
    @Transactional
    public void logout(String bearerToken, String refreshToken, HttpServletResponse response) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new UnauthorizedException("유효하지 않은 액세스 토큰입니다.");
        }

        String accessToken = jwtUtil.substringToken(bearerToken);
        jwtBlacklistService.addBlacklist(accessToken);

        if (refreshToken != null && !refreshToken.isEmpty()) {
            jwtBlacklistService.addBlacklist(refreshToken);
            refreshTokenService.deleteByToken(refreshToken);
        }

        Cookie deleteCookie = cookieUtil.deleteCookie("refreshToken");
        cookieUtil.addCookie(response, deleteCookie);
    }
}
