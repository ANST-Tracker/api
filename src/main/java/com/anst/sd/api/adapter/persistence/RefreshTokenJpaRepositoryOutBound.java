package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepositoryOutBound extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
