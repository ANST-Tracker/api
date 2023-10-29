package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.adapter.rest.user.dto.UserMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponse;
import com.anst.sd.api.app.api.*;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.JwtService;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.adapter.rest.dto.LoginRequest;
import com.anst.sd.api.adapter.rest.dto.SignupRequest;
import com.anst.sd.api.adapter.rest.dto.JwtResponse;
import com.anst.sd.api.domain.Device;
import com.anst.sd.api.domain.RefreshToken;
import com.anst.sd.api.domain.Role;
import com.anst.sd.api.app.api.user.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.anst.sd.api.app.api.AuthErrorMessages.INVALID_PASSWORD;
import static com.anst.sd.api.app.api.AuthErrorMessages.USER_DOESNT_EXISTS;
import static com.anst.sd.api.app.api.ClientErrorMessages.EMAIL_ALREADY_TAKEN;
import static com.anst.sd.api.app.api.ClientErrorMessages.USERNAME_ALREADY_TAKEN;
import static com.anst.sd.api.app.api.ErrorMessages.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final DeviceRepository deviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public JwtResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthException(USER_DOESNT_EXISTS));

        if (user.getPassword() != null && !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
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
        var tokens = jwtService.generateAccessRefreshTokens(user, curDevice.getId(), ERole.USER);
        var refreshToken = new RefreshToken();
        refreshToken.setToken(tokens.getRefreshToken());
        refreshToken.setUser(user);
        refreshToken.setDevice(curDevice);
        refreshTokenRepository.save(refreshToken);

        return new JwtResponse(
                tokens.getAccessToken(),
                tokens.getRefreshToken());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserInfoResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ClientException(USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ClientException(EMAIL_ALREADY_TAKEN);
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository
                .findByName(ERole.USER)
                .orElseThrow(() -> new ServerException(INTERNAL_SERVER_ERROR));
        roles.add(userRole);
        user.setRoles(roles);
        var registeredUser = userRepository.save(user);

        return UserMapper.toApi(registeredUser);
    }

    public UserInfoResponse getUserInfo(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_DOESNT_EXISTS);
        }
        return UserMapper.toApi(user.get());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserInfoResponse deleteUser(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_DOESNT_EXISTS);
        }
        userRepository.deleteById(userId);
        return UserMapper.toApi(user.get());
    }
}
