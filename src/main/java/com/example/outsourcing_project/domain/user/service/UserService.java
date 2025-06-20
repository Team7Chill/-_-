package com.example.outsourcing_project.domain.user.service;

import com.example.outsourcing_project.domain.auth.service.JwtBlacklistService;
import com.example.outsourcing_project.domain.auth.service.RefreshTokenService;
import com.example.outsourcing_project.domain.user.controller.dto.RegisterRequestDto;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.domain.user.domain.repository.UserRepository;
import com.example.outsourcing_project.global.enums.UserRoleEnum;
import com.example.outsourcing_project.global.exception.ConflictException;
import com.example.outsourcing_project.global.exception.NotFoundException;
import com.example.outsourcing_project.global.security.cookie.CookieUtil;
import com.example.outsourcing_project.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;

    @Transactional
    public void register(RegisterRequestDto dto) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("이미 사용 중인 이메일입니다.");
        }

        // 아이디 중복 체크
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 유저 엔티티 생성
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .password(encodedPassword)
                .role(UserRoleEnum.USER)
                .tokenVersion(1)
                .build();

        // 저장
        userRepository.save(user);

    }

    @Transactional
    public void delete(String accessToken, String refreshToken,Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("요청에 해당하는 사용자 정보를 찾을 수 없습니다."));

        jwtBlacklistService.addBlacklist(accessToken);

        if (refreshToken != null && !refreshToken.isEmpty()) {
            jwtBlacklistService.addBlacklist(refreshToken);
            refreshTokenService.deleteByToken(refreshToken);
        }

        Cookie deleteCookie = cookieUtil.deleteCookie("refreshToken");
        user.delete();
    }
}
