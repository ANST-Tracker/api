package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.DeviceRepository;
import com.anst.sd.api.app.api.RefreshTokenRepository;
import com.anst.sd.api.app.api.ServerException;
import com.anst.sd.api.app.api.security.JwtResponse;
import com.anst.sd.api.app.api.security.LoginRequest;
import com.anst.sd.api.app.api.user.LoginUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.Device;
import com.anst.sd.api.domain.RefreshToken;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.AuthException;
import com.anst.sd.api.security.ERole;
import com.anst.sd.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.anst.sd.api.security.AuthErrorMessages.INVALID_PASSWORD;
import static com.anst.sd.api.security.AuthErrorMessages.USER_DOESNT_EXISTS;

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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public JwtResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthException(USER_DOESNT_EXISTS));

        log.info("Logging user with id {}", user.getId());

        if (user.getPassword() != null && !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("User with id {} has bad credentials", user.getId());
            throw new AuthException(INVALID_PASSWORD);
        }

        var device = deviceRepository.findByDeviceToken(loginRequest.getDeviceToken());

        if (device.isPresent()) {
            var currentDevice = device.get();
            currentDevice.setLastLogin(Instant.now());
            deviceRepository.save(currentDevice);
        } else {
            var modelDevice = new Device();
            modelDevice.setLastLogin(Instant.now());
            modelDevice.setCreated(Instant.now());
            modelDevice.setDeviceToken(loginRequest.getDeviceToken());
            modelDevice.setUser(user);
            deviceRepository.save(modelDevice);
        }

        var curDevice = deviceRepository.findByDeviceToken(loginRequest.getDeviceToken())
                .orElseThrow(() -> new ServerException("Device should exist but not found"));
        var tokens = jwtService.generateAccessRefreshTokens(user.getUsername(), user.getId(), curDevice.getId(), ERole.USER);
        var refreshToken = new RefreshToken();
        refreshToken.setToken(tokens.getRefreshToken());
        refreshToken.setUser(user);
        refreshToken.setDevice(curDevice);
        refreshTokenRepository.save(refreshToken);

        log.debug("User has logged in account with id {}", user.getId());
        return new JwtResponse(
                tokens.getAccessToken(),
                tokens.getRefreshToken());
    }
}
