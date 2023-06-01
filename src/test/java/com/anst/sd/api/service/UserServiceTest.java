package com.anst.sd.api.service;

import com.anst.sd.api.builder.UserMapper;
import com.anst.sd.api.model.dto.response.UserInfoResponse;
import com.anst.sd.api.model.entity.User;
import com.anst.sd.api.model.dto.request.LoginRequest;
import com.anst.sd.api.model.dto.request.SignupRequest;
import com.anst.sd.api.model.dto.response.RefreshResponse;
import com.anst.sd.api.model.entity.Device;
import com.anst.sd.api.model.entity.RefreshToken;
import com.anst.sd.api.model.entity.Role;
import com.anst.sd.api.model.enums.ERole;
import com.anst.sd.api.dao.DeviceRepository;
import com.anst.sd.api.dao.RefreshTokenRepository;
import com.anst.sd.api.dao.RoleRepository;
import com.anst.sd.api.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    JwtService jwtService;
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void loginUser() {
        User user = new User();
        Device device = new Device();
        device.setUser(user);
        device.setId(1L);
        device.setDeviceToken(UUID.randomUUID());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceToken(device.getDeviceToken());
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        user.setId(1L);
        user.setPassword("password");
        RefreshResponse refreshResponse = new RefreshResponse(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
        user.setPassword("password");
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(deviceRepository.findByDeviceToken(loginRequest.getDeviceToken())).thenReturn(Optional.of(device));
        when(jwtService.generateAccessRefreshTokens(user,device.getId(), ERole.USER)).thenReturn(refreshResponse);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setDevice(device);
        refreshToken.setToken(refreshResponse.getRefreshToken());

        userService.loginUser(loginRequest);

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void registerUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("username");
        signupRequest.setPassword("password");
        signupRequest.setUsername("username");
        signupRequest.setEmail("email@mail.ru");
        signupRequest.setFirstName("first");
        signupRequest.setLastName("last");
        User user = new User();
        user.setId(1L);
        user.setLastName("l");
        user.setFirstName("f");
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.USER);
        user.setUsername("username");
        user.setPassword("password");
        user.setRoles(Set.of(role));
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                "a","a","a","a",Set.of());
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(signupRequest.getPassword());
        when(roleRepository.findByName(ERole.USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        try (MockedStatic<UserMapper> mockedStatic = mockStatic(UserMapper.class)){
            mockedStatic.when(() -> UserMapper.toApi(user)).thenReturn(userInfoResponse);
        }

        userService.registerUser(signupRequest);

        verify(userRepository).save(any(User.class));
    }


    @Test
    void getUserInfo() {
        User user = new User();
        user.setFirstName("f");
        user.setLastName("l");
        user.setRoles(Set.of());
        user.setId(1L);
        user.setEmail("mail");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserInfoResponse userInfoResponse = userService.getUserInfo(user.getId());

        assertEquals(user.getUsername(),userInfoResponse.getUsername());
        assertNotNull(userInfoResponse);
    }

    @Test
    void deleteUser() {
        User user = new User();
        user.setFirstName("f");
        user.setLastName("l");
        user.setRoles(Set.of());
        user.setId(1L);
        user.setEmail("mail");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository).deleteById(user.getId());
    }
}