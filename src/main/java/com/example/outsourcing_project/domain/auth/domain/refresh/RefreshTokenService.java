package com.example.outsourcing_project.domain.auth.domain.refresh;

import com.example.outsourcing_project.domain.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

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

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenValue(token);
    }


    /**
     * DB에 저장된 리프레시 토큰의 유효성 검사
     */
    public boolean isValidRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = findByToken(token);
        return refreshToken.isPresent()
                && !refreshToken.get().isRevoked()
                && refreshToken.get().getExpiryDate().isAfter(Instant.now());
    }

    /**
     * DB에서 리프레시 토큰 삭제
     */
    public void deleteByToken(String token) {
        refreshTokenRepository.findByTokenValue(token)
                .ifPresent(refreshTokenRepository::delete);
    }
}
