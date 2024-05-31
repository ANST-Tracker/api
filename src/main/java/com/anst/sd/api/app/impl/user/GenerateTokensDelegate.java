package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.app.api.security.RefreshTokenRepository;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.security.RefreshToken;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.anst.sd.api.security.domain.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GenerateTokensDelegate {
    private final DeviceRepository deviceRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtResponse generate(User user, UUID deviceToken) {
        var curDevice = deviceRepository.findByDeviceToken(deviceToken)
                .orElse(Device.createDevice(deviceToken, user));
        curDevice = deviceRepository.save(curDevice);
        var tokens = jwtService.generateAccessRefreshTokens(
                user.getUsername(), user.getId(), curDevice.getId(), ERole.USER);
        var refreshToken = new RefreshToken();
        refreshToken.setToken(tokens.getRefreshToken());
        refreshToken.setUser(user);
        refreshToken.setDevice(curDevice);
        refreshTokenRepository.save(refreshToken);
        return tokens;
    }
}
