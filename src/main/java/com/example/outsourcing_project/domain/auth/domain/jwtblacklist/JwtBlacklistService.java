package com.example.outsourcing_project.domain.auth.domain.jwtblacklist;

import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final JwtUtil jwtUtil;

    // 블랙리스트에 토큰 저장
    public void addBlacklist(String token) {
        String jti = jwtUtil.extractJti(token);
        JwtBlacklistToken blacklistToken = new JwtBlacklistToken(jti, Instant.now());
        jwtBlacklistRepository.save(blacklistToken);
    }

    public boolean isBlacklistedByJti(String jti) {
        return jwtBlacklistRepository.existsByJti(jti);
    }
}
