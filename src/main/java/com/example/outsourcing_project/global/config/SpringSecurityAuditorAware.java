package com.example.outsourcing_project.global.config;

import com.example.outsourcing_project.global.security.Jwt.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보 없거나 익명 사용자인 경우
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("anonymous");
        }

        Object principal = authentication.getPrincipal();

        // 사용자명 추출
        if (principal instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        } else if (principal instanceof String username) {
            return Optional.of(username);
        }
        return Optional.of("anonymous");
    }
}
