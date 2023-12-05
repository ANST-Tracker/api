//package com.anst.sd.api.service;
//
//import com.anst.sd.api.adapter.rest.user.dto.UserMapper;
//import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponse;
//import com.anst.sd.api.fw.security.JwtService;
//import com.anst.sd.api.app.impl.user.UserService;
//import com.anst.sd.api.domain.user.User;
//import com.anst.sd.api.adapter.rest.dto.LoginRequest;
//import com.anst.sd.api.adapter.rest.dto.SignupRequest;
//import com.anst.sd.api.adapter.rest.dto.RefreshResponse;
//import com.anst.sd.api.domain.Device;
//import com.anst.sd.api.domain.security.RefreshToken;
//import com.anst.sd.api.domain.security.Role;
//import com.anst.sd.api.fw.security.ERole;
//import com.anst.sd.api.adapter.persistence.DeviceJpaRepository;
//import com.anst.sd.api.adapter.persistence.RefreshTokenJpaRepository;
//import com.anst.sd.api.adapter.persistence.RoleJpaRepository;
//import com.anst.sd.api.adapter.persistence.UserJpaRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//    @Mock
//    UserJpaRepository userJpaRepository;
//    @Mock
//    RoleJpaRepository roleJpaRepository;
//    @Mock
//    JwtService jwtService;
//    @Mock
//    DeviceJpaRepository deviceJpaRepository;
//    @Mock
//    RefreshTokenJpaRepository refreshTokenJpaRepository;
//    @Mock
//    PasswordEncoder passwordEncoder;
//    @InjectMocks
//    UserService userService;
//
//    @Test
//    void loginUser() {
//        User user = new User();
//        Device device = new Device();
//        device.setUser(user);
//        device.setId(1L);
//        device.setDeviceToken(UUID.randomUUID());
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setDeviceToken(device.getDeviceToken());
//        loginRequest.setUsername("username");
//        loginRequest.setPassword("password");
//        user.setId(1L);
//        user.setPassword("password");
//        RefreshResponse refreshResponse = new RefreshResponse(
//                UUID.randomUUID().toString(),
//                UUID.randomUUID().toString()
//        );
//        user.setPassword("password");
//        when(userJpaRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
//        when(deviceJpaRepository.findByDeviceToken(loginRequest.getDeviceToken())).thenReturn(Optional.of(device));
//        when(jwtService.generateAccessRefreshTokens(user,device.getId(), ERole.USER)).thenReturn(refreshResponse);
//
//        RefreshToken refreshToken = new RefreshToken();
//        refreshToken.setUser(user);
//        refreshToken.setDevice(device);
//        refreshToken.setToken(refreshResponse.getRefreshToken());
//
//        userService.loginUser(loginRequest);
//
//        verify(refreshTokenJpaRepository).save(any(RefreshToken.class));
//    }
//
//    @Test
//    void registerUser() {
//        SignupRequest signupRequest = new SignupRequest();
//        signupRequest.setUsername("username");
//        signupRequest.setPassword("password");
//        signupRequest.setUsername("username");
//        signupRequest.setEmail("email@mail.ru");
//        signupRequest.setFirstName("first");
//        signupRequest.setLastName("last");
//        User user = new User();
//        user.setId(1L);
//        user.setLastName("l");
//        user.setFirstName("f");
//        Role role = new Role();
//        role.setId(1);
//        role.setName(ERole.USER);
//        user.setUsername("username");
//        user.setPassword("password");
//        user.setRoles(Set.of(role));
//        UserInfoResponse userInfoResponse = new UserInfoResponse(
//                "a","a","a","a",Set.of());
//        when(userJpaRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
//        when(userJpaRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
//        when(passwordEncoder.encode(anyString())).thenReturn(signupRequest.getPassword());
//        when(roleJpaRepository.findByName(ERole.USER)).thenReturn(Optional.of(role));
//        when(userJpaRepository.save(any(User.class))).thenReturn(user);
//        try (MockedStatic<UserMapper> mockedStatic = mockStatic(UserMapper.class)){
//            mockedStatic.when(() -> UserMapper.toApi(user)).thenReturn(userInfoResponse);
//        }
//
//        userService.registerUser(signupRequest);
//
//        verify(userJpaRepository).save(any(User.class));
//    }
//
//
//    @Test
//    void getUserInfo() {
//        User user = new User();
//        user.setFirstName("f");
//        user.setLastName("l");
//        user.setRoles(Set.of());
//        user.setId(1L);
//        user.setEmail("mail");
//
//        when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(user));
//
//        UserInfoResponse userInfoResponse = userService.getUserInfo(user.getId());
//
//        assertEquals(user.getUsername(),userInfoResponse.getUsername());
//        assertNotNull(userInfoResponse);
//    }
//
//    @Test
//    void deleteUser() {
//        User user = new User();
//        user.setFirstName("f");
//        user.setLastName("l");
//        user.setRoles(Set.of());
//        user.setId(1L);
//        user.setEmail("mail");
//
//        when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(user));
//
//        userService.deleteUser(user.getId());
//
//        verify(userJpaRepository).deleteById(user.getId());
//    }
//}