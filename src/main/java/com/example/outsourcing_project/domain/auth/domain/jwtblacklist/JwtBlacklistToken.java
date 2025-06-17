package com.example.outsourcing_project.domain.auth.domain.jwtblacklist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "jwt_blacklist")
@Getter
@NoArgsConstructor
public class JwtBlacklistToken {

    @Id
    @Column(length = 36)
    private String jti;

    @Column(nullable = false)
    private Instant blacklistedAt;

    public JwtBlacklistToken(String jti, Instant blacklistedAt) {
        this.jti = jti;
        this.blacklistedAt = blacklistedAt;
    }
}

