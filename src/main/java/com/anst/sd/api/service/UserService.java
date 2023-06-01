package com.anst.sd.api.service;

import com.anst.sd.api.builder.UserMapper;
import com.anst.sd.api.model.dto.response.UserInfoResponse;
import com.anst.sd.api.model.entity.User;
import com.anst.sd.api.model.exception.AuthException;
import com.anst.sd.api.model.dto.request.LoginRequest;
import com.anst.sd.api.model.dto.request.SignupRequest;
import com.anst.sd.api.model.dto.response.JwtResponse;
import com.anst.sd.api.model.entity.Device;
import com.anst.sd.api.model.entity.RefreshToken;
import com.anst.sd.api.model.entity.Role;
import com.anst.sd.api.model.enums.ERole;
import com.anst.sd.api.model.exception.ClientException;
import com.anst.sd.api.model.exception.ServerException;
import com.anst.sd.api.dao.DeviceRepository;
import com.anst.sd.api.dao.RefreshTokenRepository;
import com.anst.sd.api.dao.RoleRepository;
import com.anst.sd.api.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.anst.sd.api.model.constant.AuthErrorMessages.INVALID_PASSWORD;
import static com.anst.sd.api.model.constant.AuthErrorMessages.USER_DOESNT_EXISTS;
import static com.anst.sd.api.model.constant.ClientErrorMessages.EMAIL_ALREADY_TAKEN;
import static com.anst.sd.api.model.constant.ClientErrorMessages.USERNAME_ALREADY_TAKEN;
import static com.anst.sd.api.model.constant.ErrorMessages.INTERNAL_SERVER_ERROR;

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
