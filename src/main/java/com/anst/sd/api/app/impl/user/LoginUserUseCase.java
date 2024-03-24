package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.app.api.security.RefreshTokenRepository;
import com.anst.sd.api.app.api.user.LoginUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.security.RefreshToken;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.AuthException;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.anst.sd.api.security.domain.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.anst.sd.api.security.app.api.AuthErrorMessages.INVALID_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserUseCase implements LoginUserInBound {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeviceRepository deviceRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public JwtResponse login(String username, String password, UUID deviceToken) {
        log.info("Logging user with username {}", username);
        User user = userRepository.getByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(INVALID_PASSWORD);
        }

        String tokenTgId = jwtService.getJwtAuth().getTelegramId();
        if (!user.getTelegramId().equals(tokenTgId)) {
            throw new AuthException("Trying to login user with tgId %s with token for tgId %s".formatted(user.getTelegramId(), tokenTgId));
        }

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
