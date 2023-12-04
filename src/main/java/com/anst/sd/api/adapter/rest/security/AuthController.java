package com.anst.sd.api.adapter.rest.security;

import com.anst.sd.api.adapter.rest.security.dto.*;
import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.app.api.security.RefreshTokenInBound;
import com.anst.sd.api.app.api.user.LoginUserInBound;
import com.anst.sd.api.app.api.user.RegisterUserInBound;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RefreshTokenInBound refreshTokenInBound;
    private final LoginUserInBound loginUserInBound;
    private final RegisterUserInBound registerUserInBound;
    private final UserDtoMapper userDtoMapper;
    private final SignupRequestDomainMapper signUpRequestDomainMapper;
    private final JwtResponseDtoMapper jwtResponseDtoMapper;

    @Operation(
            summary = "Refresh an access token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access token refreshed successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@Valid @RequestBody RefreshRequestDto request) {
        JwtResponse response = refreshTokenInBound.refresh(request.getRefreshToken());
        return ResponseEntity.ok(jwtResponseDtoMapper.mapToDto(response));
    }

    @Operation(
            summary = "User authentication",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDto> loginUser(@Valid @RequestBody LoginRequestDto request) {
        JwtResponse response = loginUserInBound.login(
                request.getUsername(), request.getPassword(), request.getDeviceToken());
        return ResponseEntity.ok(jwtResponseDtoMapper.mapToDto(response));
    }

    @Operation(
            summary = "User registration",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping("/signup")
    public ResponseEntity<UserInfoDto> registerUser(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        User registeredUser = registerUserInBound.register(signUpRequestDomainMapper.mapToDomain(signUpRequestDto));
        return ResponseEntity.ok(userDtoMapper.mapToDto(registeredUser));
    }
}






