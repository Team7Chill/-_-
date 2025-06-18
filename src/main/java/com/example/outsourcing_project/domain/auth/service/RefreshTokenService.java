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
    private final CookieUtil cookieUtil;
    private String currentRefreshToken;

    /**
     * 요청에서 쿠키로부터 리프레시 토큰을 꺼내
     * 토큰 유효성 검사 후 만료시간 갱신하고,
     * 새 액세스 토큰을 생성하여 반환한다.
     */
    public String refreshAccessTokenFromRequest(HttpServletRequest request) {
        // 1. 쿠키에서 리프레시 토큰 추출
        currentRefreshToken = cookieUtil.getCookie(request, "refreshToken")
                .orElseThrow(() -> new UnauthorizedException("리프레시 토큰이 존재하지 않습니다."))
                .getValue();

        // 2. 토큰 유효성 검사 및 DB 조회
        RefreshToken tokenEntity = validateAndGetToken(currentRefreshToken);

        // 3. 만료시간 갱신 후 저장
        Instant newExpiryDate = Instant.now().plusMillis(jwtUtil.getRefreshTokenExpireTime());
        tokenEntity.setExpiryDate(newExpiryDate);
        refreshTokenRepository.save(tokenEntity);

        // 4. 새 액세스 토큰 생성 및 반환
        User user = tokenEntity.getUser();
        return jwtUtil.createAccessToken(user.getId(), user.getRole());
    }

    /**
     * 현재 저장된 리프레시 토큰 값을 쿠키로 만들어
     * 응답에 추가한다.
     */
    public void setRefreshTokenCookie(HttpServletResponse response) {
        if (currentRefreshToken == null) {
            throw new UnauthorizedException("Refresh token 값이 설정되지 않았습니다.");
        }
        cookieUtil.setRefreshTokenCookie(
                response,
                currentRefreshToken,
                (int) (jwtUtil.getRefreshTokenExpireTime() / 1000)
        );
    }

    /**
     * 리프레시 토큰이 DB에 존재하고,
     * 취소되지 않았으며, 만료되지 않았는지 확인한다.
     * 유효하지 않으면 예외를 던진다.
     */
    public RefreshToken validateAndGetToken(String token) {
        return findByToken(token)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()))
                .orElseThrow(() -> new UnauthorizedException("유효하지 않거나 만료된 리프레시 토큰입니다."));
    }

    /**
     * 토큰 값으로 DB에서 리프레시 토큰 엔티티를 조회한다.
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenValue(token);
    }

    /**
     * 토큰 값으로 DB에서 리프레시 토큰을 찾아 삭제한다.
     */
    public void deleteByToken(String token) {
        findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    /**
     * 새로운 리프레시 토큰을 생성하여 DB에 저장한다.
     */
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
