package com.example.outsourcing_project.domain.auth.domain.refresh;

import com.example.outsourcing_project.domain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_value", length = 512, nullable = false, unique = true)
    private String tokenValue;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Setter
    @Column(name = "is_revoked", nullable = false)
    private boolean isRevoked = false;

}