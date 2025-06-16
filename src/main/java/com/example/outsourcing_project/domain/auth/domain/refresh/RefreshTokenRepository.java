package com.example.outsourcing_project.domain.auth.domain.refresh;

import com.example.outsourcing_project.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    List<RefreshToken> findAllByUser(User user);
}