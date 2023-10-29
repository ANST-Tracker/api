package com.anst.sd.api.service;

import com.anst.sd.api.app.impl.AuthService;
import com.anst.sd.api.fw.security.JwtAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
//    @Mock
//    JwtService jwtService;
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    DeviceRepository deviceRepository;
//    @Mock
//    RefreshTokenRepository refreshTokenRepository;
//    @Mock
//    JwtAuth jwtAuth;
    @Mock
    Authentication authenticationMock;
    @InjectMocks
    AuthService authService;

    @BeforeEach
    void initSecurityContextHolder() {
        when(authenticationMock.getCredentials()).thenReturn("mock");
        SecurityContextHolder.getContext().setAuthentication(authenticationMock);
    }
    @Test
    void getJwtAuth_success() {
        SecurityContextHolder.getContext().getAuthentication().getCredentials();
        JwtAuth afterResponse = authService.getJwtAuth();
        assertNotNull(afterResponse);
    }

//    public RefreshRequest createRefreshRequestByDefault() {
//        new RefreshRequest().setRefreshToken(UUID.randomUUID().toString());
//        return new RefreshRequest();
//    }
//
//    public RefreshToken createRefreshTokenByDefault() {
//        RefreshToken refreshToken = new RefreshToken();
//        refreshToken.setUser(createUserByDefault());
//        refreshToken.setDevice(createDeviceByDefault());
//        refreshToken.setId(1L);
//        refreshToken.setToken(UUID.randomUUID().toString());
//        return refreshToken;
//    }
//
//    public RefreshResponse createRefreshResponseByDefault() {
//        return new RefreshResponse(
//                UUID.randomUUID().toString(),
//                UUID.randomUUID().toString());
//    }

//    public Device createDeviceByDefault() {
//        Device device = new Device();
//        device.setDeviceToken(UUID.randomUUID());
//        device.setId(1L);
//        device.setUser(createUserByDefault());
//        device.setCreated(Instant.now());
//        device.setLastLogin(Instant.now()) ;
//        return device;
//    }

//    public User createUserByDefault() {
//        return User.builder()
//                .id(1L)
//                .firstName("Anton")
//                .lastName("Pestrikov")
//                .username("eridium")
//                .email("pestrikov@mail.ru")
//                .password("pestrikov123")
//                .devices(List.of())
//                .build();
//    }
}