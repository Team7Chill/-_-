package com.example.outsourcing_project.domain.auth.service;

import com.example.outsourcing_project.domain.auth.domain.refresh.RefreshToken;
import com.example.outsourcing_project.domain.auth.domain.refresh.RefreshTokenRepository;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.global.exception.UnauthorizedException;
import com.example.outsourcing_project.global.security.cookie.CookieUtil;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    private String currentRefreshToken; // 현재 요청에서 처리한 리프레시 토큰 저장용 (쿠키 세팅 시 사용)

    public String refreshAccessTokenFromRequest(HttpServletRequest request) {
        // 1. 쿠키에서 리프레시 토큰 추출
        currentRefreshToken = CookieUtil.getCookie(request, "refreshToken")
                .orElseThrow(() -> new UnauthorizedException("리프레시 토큰이 존재하지 않습니다."))
                .getValue();

        // 2. 토큰 유효성 검사 및 조회
        RefreshToken tokenEntity = validateAndGetToken(currentRefreshToken);

        // 3. 만료시간 갱신
        Instant newExpiryDate = Instant.now().plusMillis(jwtUtil.getRefreshTokenExpireTime());
        tokenEntity.setExpiryDate(newExpiryDate);
        refreshTokenRepository.save(tokenEntity);

        // 4. 새로운 AccessToken 생성 및 반환
        User user = tokenEntity.getUser();
        return jwtUtil.createAccessToken(user.getId(), user.getRole());
    }

    public void setRefreshTokenCookie(HttpServletResponse response) {
        if (currentRefreshToken == null) {
            throw new UnauthorizedException("Refresh token 값이 설정되지 않았습니다.");
        }
        CookieUtil.setRefreshTokenCookie(
                response,
                currentRefreshToken,
                (int) (jwtUtil.getRefreshTokenExpireTime() / 1000)
        );
    }

    public RefreshToken validateAndGetToken(String token) {
        return findByToken(token)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()))
                .orElseThrow(() -> new UnauthorizedException("유효하지 않거나 만료된 리프레시 토큰입니다."));
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenValue(token);
    }

    public void deleteByToken(String token) {
        findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    public RefreshToken createRefreshToken(User user, String token, Instant issuedAt, Instant expiryDate) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenValue(token)
                .issuedAt(issuedAt)
                .expiryDate(expiryDate)
                .isRevoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
}

