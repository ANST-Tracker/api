package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.security.RefreshTokenRepository;
import com.anst.sd.api.domain.security.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token);
    }

    @Override
    public void deleteById(Long id) {
        refreshTokenJpaRepository.deleteById(id);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(refreshToken);
    }
}
