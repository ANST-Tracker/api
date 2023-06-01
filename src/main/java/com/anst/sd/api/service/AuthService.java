package com.anst.sd.api.service;

import com.anst.sd.api.model.constant.AuthErrorMessages;
import com.anst.sd.api.model.entity.RefreshToken;
import com.anst.sd.api.model.exception.AuthException;
import com.anst.sd.api.model.dto.request.RefreshRequest;
import com.anst.sd.api.model.dto.response.RefreshResponse;
import com.anst.sd.api.dao.DeviceRepository;
import com.anst.sd.api.dao.RefreshTokenRepository;
import com.anst.sd.api.dao.UserRepository;
import com.anst.sd.api.security.JwtAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuth getJwtAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuth) {
            return ((JwtAuth) authentication);
        } else {
            return JwtAuth.builder().build();
        }
    }
    public RefreshResponse refresh(RefreshRequest request) {
        if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
            throw new AuthException(AuthErrorMessages.INVALID_REFRESH_TOKEN);
        }

        var claims = jwtService.getRefreshClaims(request.getRefreshToken());
        var user = userRepository.findById(Long.parseLong(claims.getUserId()))
                .orElseThrow(() -> new AuthException(AuthErrorMessages.USER_NOT_FOUND));
        var device = deviceRepository.findById(Long.parseLong(claims.getDeviceId()))
                .orElseThrow(() -> new AuthException(AuthErrorMessages.DEVICE_NOT_FOUND));
        var role = claims.getRole();

        var currentRefreshToken = refreshTokenRepository.findByToken(request.getRefreshToken());

        if (currentRefreshToken.isEmpty()) {
            throw new AuthException(AuthErrorMessages.REFRESH_TOKEN_DOESNT_EXISTS);
        }

        refreshTokenRepository.deleteById(currentRefreshToken.get().getId());
        if (!jwtService.validateAccessTokenLifetime(device.getId())) {
            throw new AuthException(AuthErrorMessages.SUSPICIOUS_ACTIVITY);
        }

        var tokens = jwtService.generateAccessRefreshTokens(user, device.getId(), role);
        var newRefresh = new RefreshToken();
        newRefresh.setUser(user);
        newRefresh.setDevice(device);
        newRefresh.setToken(tokens.getRefreshToken());

        refreshTokenRepository.save(newRefresh);

        return tokens;
    }
}