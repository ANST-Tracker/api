package com.anst.sd.api.app.impl.security;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.task.security.RefreshTokenUseCase;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.AuthErrorMessages;
import com.anst.sd.api.security.app.api.AuthException;
import com.anst.sd.api.security.app.impl.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RefreshTokenUseCaseTest extends AbstractUnitTest {
    private static final Long USER_ID = 123L;
    private static final Long DEVICE_ID = 456L;
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String INPUT_TOKEN = "TEST_TOKEN";
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @InjectMocks
    private RefreshTokenUseCase useCase;
    private User user;
    private Device device;
    private JwtService.ClaimsHolder claimsHolder;

    @BeforeEach
    void setUp() {
        user = createTestUser();
        device = createDevice();
        claimsHolder = createClaims();
    }

    @Test
    void refresh_invalid_refresh_token() {
        when(jwtService.validateRefreshToken((INPUT_TOKEN))).thenReturn(false);

        AuthException thrown = assertThrows(AuthException.class, () -> useCase.refresh(INPUT_TOKEN));

        assertEquals(AuthErrorMessages.INVALID_REFRESH_TOKEN, thrown.getMessage());
    }

    @Test
    void refresh_device_not_found() {
        when(jwtService.validateRefreshToken((INPUT_TOKEN))).thenReturn(true);
        when(jwtService.getRefreshClaims(anyString())).thenReturn(claimsHolder);
        when(userRepository.getById(USER_ID)).thenReturn(user);
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.empty());

        AuthException thrown = assertThrows(AuthException.class, () -> useCase.refresh(INPUT_TOKEN));

        assertEquals(AuthErrorMessages.DEVICE_NOT_FOUND, thrown.getMessage());
    }

    @Test
    void refresh_token_not_exists() {
        when(jwtService.validateRefreshToken((INPUT_TOKEN))).thenReturn(true);
        when(jwtService.getRefreshClaims(anyString())).thenReturn(claimsHolder);
        when(userRepository.getById(USER_ID)).thenReturn(user);
        device.setToken(INPUT_TOKEN + "any");
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        AuthException thrown = assertThrows(AuthException.class, () -> useCase.refresh(INPUT_TOKEN));

        assertEquals(AuthErrorMessages.INVALID_REFRESH_TOKEN, thrown.getMessage());
    }

    @Test
    void refresh_suspicious_activity() {
        when(jwtService.validateRefreshToken((INPUT_TOKEN))).thenReturn(true);
        when(jwtService.getRefreshClaims(anyString())).thenReturn(claimsHolder);
        when(userRepository.getById(USER_ID)).thenReturn(user);
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));
        when(jwtService.validateAccessTokenLifetime(device.getId())).thenReturn(false);

        AuthException thrown = assertThrows(AuthException.class, () -> useCase.refresh(INPUT_TOKEN));

        assertEquals(AuthErrorMessages.SUSPICIOUS_ACTIVITY, thrown.getMessage());
    }

    private Device createDevice() {
        Device device = new Device();
        device.setCreated(Instant.now());
        device.setUser(createTestUser());
        device.setToken(INPUT_TOKEN);
        return device;
    }

    private JwtService.ClaimsHolder createClaims() {
        Claims preparedClaims = new DefaultClaims();
        preparedClaims.put("userId", USER_ID.toString());
        preparedClaims.put("deviceId", DEVICE_ID.toString());
        preparedClaims.put("role", ADMIN_ROLE);

        return new JwtService.ClaimsHolder(preparedClaims);
    }
}