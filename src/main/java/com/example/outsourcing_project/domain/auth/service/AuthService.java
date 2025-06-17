package com.example.outsourcing_project.domain.auth.service;

import com.example.outsourcing_project.domain.auth.controller.dto.ApiResponse;
import com.example.outsourcing_project.domain.auth.domain.jwtblacklist.JwtBlacklistService;
import com.example.outsourcing_project.domain.auth.domain.refresh.RefreshTokenService;
import com.example.outsourcing_project.domain.auth.controller.dto.LoginRequest;
import com.example.outsourcing_project.domain.auth.service.dto.LoginResponse;
import com.example.outsourcing_project.domain.user.domain.User;
import com.example.outsourcing_project.domain.user.domain.UserRepository;
import com.example.outsourcing_project.global.security.Jwt.JwtUtil;
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

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));

//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getName(), user.getEmail(), user.getRole());

        String refreshToken = jwtUtil.createRefreshToken();

        // 현재 시간과 만료시간
        Instant issuedAt = Instant.now();
        Instant expiryDate = issuedAt.plusMillis(jwtUtil.getRefreshTokenExpireTime());

        // RefreshToken DB 저장
        refreshTokenService.createRefreshToken(user, jwtUtil.substringToken(refreshToken), issuedAt, expiryDate);

        return new LoginResponse(accessToken, refreshToken);
    }

    public ApiResponse logout(String bearerToken, String refreshToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer")) {
            throw new IllegalArgumentException("유효하지 않은 액세스 토큰입니다.");
        }
        // AccessToken 처리
        String accessToken = jwtUtil.substringToken(bearerToken);
        jwtBlacklistService.addBlacklist(accessToken);

        // RefreshToken 처리 (optional)
//        if (refreshToken != null && !refreshToken.isEmpty()) {
//            String refreshJti = jwtUtil.extractJti(refreshToken);
//            jwtBlacklistService.addBlacklist(refreshJti);
//        }

        return new ApiResponse(true, "로그아웃 성공", Instant.now().toString());
    }


}
