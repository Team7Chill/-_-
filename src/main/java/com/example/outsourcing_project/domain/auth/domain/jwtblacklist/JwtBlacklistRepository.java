package com.example.outsourcing_project.domain.auth.domain.jwtblacklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklistToken, String> {
    boolean existsByJti(String jti);
}
