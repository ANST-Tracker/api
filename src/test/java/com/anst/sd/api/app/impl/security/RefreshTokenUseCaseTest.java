package com.anst.sd.api.app.impl.security;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.DeviceRepository;
import com.anst.sd.api.app.api.security.RefreshTokenRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.security.RefreshToken;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.AuthErrorMessages;
import com.anst.sd.api.security.AuthException;
import com.anst.sd.api.security.JwtService;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RefreshTokenUseCaseTest extends AbstractUnitTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenUseCase useCase;
    private static final Long USER_ID = 123L;
    private static final Long DEVICE_ID = 456L;
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String INPUT_TOKEN = "TEST_TOKEN";
    private User user;
    private Device device;
    private RefreshToken refreshToken;
    private JwtService.ClaimsHolder claimsHolder;

    @BeforeEach
    void setUp() {
        user = createUser();
        device = createDevice();
        refreshToken = createRefreshToken();
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
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));
        when(refreshTokenRepository.findByToken(INPUT_TOKEN)).thenReturn(Optional.empty());

        AuthException thrown = assertThrows(AuthException.class, () -> useCase.refresh(INPUT_TOKEN));

        assertEquals(AuthErrorMessages.REFRESH_TOKEN_DOESNT_EXISTS, thrown.getMessage());
    }

    @Test
    void refresh_suspicious_activity() {
        when(jwtService.validateRefreshToken((INPUT_TOKEN))).thenReturn(true);
        when(jwtService.getRefreshClaims(anyString())).thenReturn(claimsHolder);
        when(userRepository.getById(USER_ID)).thenReturn(user);
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));
        when(refreshTokenRepository.findByToken(INPUT_TOKEN)).thenReturn(Optional.of(refreshToken));
        when(jwtService.validateAccessTokenLifetime(device.getId())).thenReturn(false);

        AuthException thrown = assertThrows(AuthException.class, () -> useCase.refresh(INPUT_TOKEN));

        assertEquals(AuthErrorMessages.SUSPICIOUS_ACTIVITY, thrown.getMessage());
    }

    private Device createDevice() {
        Device device = new Device();
        device.setDeviceToken(UUID.randomUUID());
        device.setCreated(Instant.now());
        device.setUser(createUser());
        return device;
    }

    private User createUser() {
        User user = new User();
        user.setFirstName("test");
        user.setLastName("test");
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test");
        return user;
    }

    private RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreated(Instant.now());
        refreshToken.setUser(createUser());
        refreshToken.setDevice(createDevice());
        return refreshToken;
    }

    private JwtService.ClaimsHolder createClaims() {
        Claims claims = new Claims() {
            private Map<String, Object> claimsMap = new HashMap<>();

            @Override
            public String getIssuer() {
                return null;
            }

            @Override
            public Claims setIssuer(String s) {
                return null;
            }

            @Override
            public String getSubject() {
                return null;
            }

            @Override
            public Claims setSubject(String s) {
                return null;
            }

            @Override
            public String getAudience() {
                return null;
            }

            @Override
            public Claims setAudience(String s) {
                return null;
            }

            @Override
            public Date getExpiration() {
                return null;
            }

            @Override
            public Claims setExpiration(Date date) {
                return null;
            }

            @Override
            public Date getNotBefore() {
                return null;
            }

            @Override
            public Claims setNotBefore(Date date) {
                return null;
            }

            @Override
            public Date getIssuedAt() {
                return null;
            }

            @Override
            public Claims setIssuedAt(Date date) {
                return null;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public Claims setId(String s) {
                return null;
            }

            @Override
            public <T> T get(String s, Class<T> aClass) {
                Object value = claimsMap.get(s);
                if (value == null) {
                    return null;
                }
                if (aClass.isInstance(value)) {
                    return aClass.cast(value);
                }
                throw new IllegalArgumentException("Cannot cast to " + aClass.getName());
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @Override
            public Object get(Object key) {
                return claimsMap.get(key);
            }

            @Nullable
            @Override
            public Object put(String key, Object value) {
                return claimsMap.put(key, value);
            }

            @Override
            public Object remove(Object key) {
                return null;
            }

            @Override
            public void putAll(@NotNull Map<? extends String, ?> m) {

            }

            @Override
            public void clear() {

            }

            @NotNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NotNull
            @Override
            public Collection<Object> values() {
                return null;
            }

            @NotNull
            @Override
            public Set<Entry<String, Object>> entrySet() {
                return null;
            }
        };
        claims.put("userId", USER_ID.toString());
        claims.put("deviceId", DEVICE_ID.toString());
        claims.put("role", ADMIN_ROLE);

        return new JwtService.ClaimsHolder(claims);
    }

}