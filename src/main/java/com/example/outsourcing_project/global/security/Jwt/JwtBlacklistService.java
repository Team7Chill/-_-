package com.example.outsourcing_project.global.security.Jwt;

import com.example.outsourcing_project.domain.auth.domain.jwtblacklist.JwtBlacklistRepository;
import com.example.outsourcing_project.domain.auth.domain.jwtblacklist.JwtBlacklistToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JwtBlacklistRepository jwtBlacklistRepository;

    public void addBlacklist(String token) {
        JwtBlacklistToken blacklistToken = new JwtBlacklistToken(token, Instant.now());
        jwtBlacklistRepository.save(blacklistToken);
    }

    public boolean isBlacklisted(String token) {
        return jwtBlacklistRepository.existsByToken(token);
    }
}