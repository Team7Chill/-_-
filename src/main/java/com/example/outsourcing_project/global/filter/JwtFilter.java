package com.example.outsourcing_project.global.filter;

import com.example.outsourcing_project.domain.auth.service.JwtBlacklistService;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.domain.user.domain.repository.UserRepository;
import com.example.outsourcing_project.global.exception.UnauthorizedException;
import com.example.outsourcing_project.global.security.jwt.CustomUserDetails;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final JwtBlacklistService jwtBlacklistService;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/login") || requestURI.startsWith("/api/signup")) {
            filterChain.doFilter(request, response); // 바로 다음 필터로 넘김
            return;
        }

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            throw new UnauthorizedException("JWT 토큰이 필요합니다.");
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        if (!jwtUtil.validateToken(jwt)) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }

        String jti = jwtUtil.extractJti(jwt);
        if (jwtBlacklistService.isBlacklistedByJti(jti)) {
            throw new UnauthorizedException("로그아웃된 토큰입니다.");
        }

        String subject = jwtUtil.extractUserId(jwt);
        Long userId = Long.parseLong(subject);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("해당 유저가 없습니다."));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities()
                    )
            );
        }

        filterChain.doFilter(request, response);
    }
}