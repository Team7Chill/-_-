package com.example.outsourcing_project.domain.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "jwt_blacklist")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtBlacklistToken {

    @Id
    @Column(length = 512)
    private String token;  // 토큰 문자열 자체를 PK로 사용

    private Instant blacklistedAt;  // 언제 블랙리스트에 추가됐는지 기록

}
