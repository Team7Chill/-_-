package com.example.outsourcing_project.domain.auth.domain.refresh;

import com.example.outsourcing_project.domain.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
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

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    public void revokeAllTokensForUser(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUser(user);
        tokens.forEach(token -> token.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
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
}
