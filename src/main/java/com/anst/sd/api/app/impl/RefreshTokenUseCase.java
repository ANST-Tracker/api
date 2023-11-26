package com.anst.sd.api.app.impl;

import com.anst.sd.api.app.api.DeviceRepository;
import com.anst.sd.api.app.api.RefreshTokenInBound;
import com.anst.sd.api.app.api.RefreshTokenRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.RefreshToken;
import com.anst.sd.api.security.AuthErrorMessages;
import com.anst.sd.api.security.AuthException;
import com.anst.sd.api.security.JwtService;
import com.anst.sd.api.security.RefreshResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase implements RefreshTokenInBound {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshResponse refresh(String refreshToken) {
        log.info("Refresh token valid process started");
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new AuthException(AuthErrorMessages.INVALID_REFRESH_TOKEN);
        }

        var claims = jwtService.getRefreshClaims(refreshToken);
        var user = userRepository.findById(Long.parseLong(claims.getUserId()))
                .orElseThrow(() -> new AuthException(AuthErrorMessages.USER_NOT_FOUND));
        var device = deviceRepository.findById(Long.parseLong(claims.getDeviceId()))
                .orElseThrow(() -> new AuthException(AuthErrorMessages.DEVICE_NOT_FOUND));
        var role = claims.getRole();

        var currentRefreshToken = refreshTokenRepository.findByToken(refreshToken);

        if (currentRefreshToken.isEmpty()) {
            throw new AuthException(AuthErrorMessages.REFRESH_TOKEN_DOESNT_EXISTS);
        }

        refreshTokenRepository.deleteById(currentRefreshToken.get().getId());
        if (!jwtService.validateAccessTokenLifetime(device.getId())) {
            throw new AuthException(AuthErrorMessages.SUSPICIOUS_ACTIVITY);
        }

        var tokens = jwtService.generateAccessRefreshTokens(user.getUsername(), user.getId(), device.getId(), role);
        var newRefresh = new RefreshToken();
        newRefresh.setUser(user);
        newRefresh.setDevice(device);
        newRefresh.setToken(tokens.getRefreshToken());

        refreshTokenRepository.save(newRefresh);

        return tokens;
    }
}
