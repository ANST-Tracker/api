package com.anst.sd.api.app.api;

import com.anst.sd.api.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);
    void deleteById(Long id);
    RefreshToken save(RefreshToken refreshToken);
}
