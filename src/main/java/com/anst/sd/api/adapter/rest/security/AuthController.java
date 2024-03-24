package com.anst.sd.api.adapter.rest.security;

import com.anst.sd.api.adapter.rest.security.dto.*;
import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.app.api.security.CheckCodeInbound;
import com.anst.sd.api.app.api.security.RefreshTokenInBound;
import com.anst.sd.api.app.api.security.SendCodeInbound;
import com.anst.sd.api.app.api.user.LoginUserInBound;
import com.anst.sd.api.app.api.user.RegisterUserException;
import com.anst.sd.api.app.api.user.RegisterUserInBound;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${security.code.return}")
    private Boolean returnCode;
    private final RefreshTokenInBound refreshTokenInBound;
    private final LoginUserInBound loginUserInBound;
    private final RegisterUserInBound registerUserInBound;
    private final CheckCodeInbound checkCodeInbound;
    private final UserDtoMapper userDtoMapper;
    private final SignupRequestDomainMapper signUpRequestDomainMapper;
    private final JwtResponseDtoMapper jwtResponseDtoMapper;
    private final SendCodeInbound sendCodeInbound;

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
        JwtResponse response = loginUserInBound.login(request.getUsername(), request.getPassword(), request.getDeviceToken());
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
    public ResponseEntity<UserInfoDto> registerUser(
            @Valid @RequestBody SignupRequestDto signUpRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new RegisterUserException(bindingResult.getAllErrors().toString());
        }
        User registeredUser = registerUserInBound.register(signUpRequestDomainMapper.mapToDomain(signUpRequestDto));
        return ResponseEntity.ok(userDtoMapper.mapToDto(registeredUser));
    }

    @PostMapping("code/send")
    public ResponseEntity<String> sendCode(@RequestBody @Valid SendCodeRequestDto sendCodeRequestDto) {
        String code = sendCodeInbound.send(sendCodeRequestDto.getTelegramId());
        if (returnCode) {
            return ResponseEntity.ok(code);
        }
        return null;
    }

    @PostMapping("code/verify")
    public ResponseEntity<JwtResponseDto> verifyCode(@RequestBody @Valid VerifyCodeRequestDto verifyCodeRequestDto) {
        String token = checkCodeInbound.check(verifyCodeRequestDto.getTelegramId(), verifyCodeRequestDto.getCode());
        return ResponseEntity.ok(new JwtResponseDto(token, null));
    }
}






