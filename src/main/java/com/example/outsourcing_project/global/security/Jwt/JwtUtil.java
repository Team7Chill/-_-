package com.example.outsourcing_project.global.security.Jwt;

import com.example.outsourcing_project.global.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 30 * 60 * 1000L; // 60분
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Long userId, String userName, String email, UserRoleEnum userRole) {
        Date now = new Date();
        String jti = UUID.randomUUID().toString();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", userRole.name())
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */

    public String createRefreshToken() {
        Date now = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setId(java.util.UUID.randomUUID().toString())  // 고유 ID
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }


    /**
     * 헤더에서 "Bearer <토큰>" 형식에서 토큰만 추출
     */

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new IllegalStateException("JWT 토큰이 필요합니다.");
    }

    /**
     * JWT 토큰에서 모든 클레임을 추출합니다.
     * @param token JWT 토큰
     * @return 클레임 객체
     */

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT JTI(id) 값 추출
    public String extractJti(String token) {
        return extractAllClaims(token).getId();
    }

    /**
     * 토큰 자체의 유효성 검증 메서드
     */

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public long getAccessTokenExpireTime() {
        return TOKEN_TIME;
    }

    public long getRefreshTokenExpireTime() {
        return REFRESH_TOKEN_TIME;
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRoles(String token) {
        return extractAllClaims(token).get("auth", String.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email",String.class);
    }

    public String extractuserId(String token) {
        return extractAllClaims(token).getSubject();
    }
 }

