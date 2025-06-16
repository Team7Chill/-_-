package com.example.outsourcing_project.global.config;

import com.example.outsourcing_project.global.filter.JwtFilter;
import com.example.outsourcing_project.global.security.Jwt.JwtBlacklistService;
import com.example.outsourcing_project.global.security.Jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtUtil,jwtBlacklistService));
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
