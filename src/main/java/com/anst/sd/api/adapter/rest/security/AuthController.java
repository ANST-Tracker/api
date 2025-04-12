package com.anst.sd.api.adapter.rest.security;

import com.anst.sd.api.adapter.rest.security.dto.*;
import com.anst.sd.api.app.api.security.CheckCodeInbound;
import com.anst.sd.api.app.api.security.RefreshTokenInBound;
import com.anst.sd.api.app.api.security.SendCodeInbound;
import com.anst.sd.api.app.api.user.LoginUserInBound;
import com.anst.sd.api.security.app.api.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RefreshTokenInBound refreshTokenInBound;
    private final LoginUserInBound loginUserInBound;
    private final CheckCodeInbound checkCodeInbound;
    private final JwtResponseDtoMapper jwtResponseDtoMapper;
    private final SendCodeInbound sendCodeInbound;
    @Value("${security.code.return}")
    private Boolean returnCode;

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
        JwtResponse response = loginUserInBound.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(jwtResponseDtoMapper.mapToDto(response));
    }

    @Operation(
            summary = "Send code for 2FA",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @PostMapping("code/send")
    public ResponseEntity<String> sendCode(@RequestBody SendCodeRequestDto sendCodeRequestDto) {
        String code = sendCodeInbound.send(sendCodeRequestDto.getTelegramId(), sendCodeRequestDto.getUsername());
        if (returnCode) {
            return ResponseEntity.ok(code);
        }
        return null;
    }

    @Operation(
            summary = "Verify code for 2FA",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @PostMapping("code/verify")
    public ResponseEntity<JwtResponseDto> verifyCode(@RequestBody @Valid VerifyCodeRequestDto verifyCodeRequestDto) {
        String token = checkCodeInbound.check(verifyCodeRequestDto.getTelegramId(), verifyCodeRequestDto.getCode(),
                verifyCodeRequestDto.getUsername());
        return ResponseEntity.ok(new JwtResponseDto(token, null));
    }
}
