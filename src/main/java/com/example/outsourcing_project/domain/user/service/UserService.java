package com.example.outsourcing_project.domain.user.service;

import com.example.outsourcing_project.domain.user.controller.dto.RegisterRequestDto;
import com.example.outsourcing_project.domain.user.domain.User;
import com.example.outsourcing_project.domain.user.domain.UserRepository;
import com.example.outsourcing_project.global.enums.UserRoleEnum;
import com.example.outsourcing_project.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequestDto dto) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("이미 사용 중인 이메일입니다.");
        }

        // 아이디 중복 체크
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 유저 엔티티 생성
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encodedPassword)
                .name(dto.getName())
                .role(UserRoleEnum.USER)
                .build();

        // 저장
        userRepository.save(user);
    }
}