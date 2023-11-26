package com.anst.sd.api.adapter.rest.security;

import com.anst.sd.api.adapter.rest.security.dto.*;
import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponseDto;
import com.anst.sd.api.app.api.ClientException;
import com.anst.sd.api.app.api.RefreshTokenInBound;
import com.anst.sd.api.app.api.user.LoginUserInBound;
import com.anst.sd.api.app.api.user.RegisterUserInBound;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.AuthException;
import com.anst.sd.api.security.RefreshResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final SignUpRequestDomainMapper signUpRequestDomainMapper;
    private final JwtResponseDtoMapper jwtResponseDtoMapper;

    @Operation(summary = "Refresh an access token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token refreshed successfully",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = RefreshResponse.class))
                    )),
            @ApiResponse(
                    responseCode = "409",
                    description = "Invalid refresh token",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = AuthException.class))
                    )
            )})
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(
            @Valid @RequestBody RefreshRequestDto request) {
        return ResponseEntity.ok(refreshTokenInBound.refresh(request.getRefreshToken()));
    }

    @Operation(summary = "User authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated successfully",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = JwtResponseDto.class))
                    )),
            @ApiResponse(
                    responseCode = "409",
                    description = "Invalid username or password",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = AuthException.class))
                    )
            )})
    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDto> authenticateUser(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(jwtResponseDtoMapper.mapToDto(loginUserInBound.loginUser(loginRequestDto.getUsername(),
                loginRequestDto.getPassword(), loginRequestDto.getDeviceToken())));
    }

    @Operation(summary = "User registration")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username or email already exists",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ClientException.class))
                    )
            )})
    @PostMapping("/signup")
    public ResponseEntity<UserInfoResponseDto> registerUser(
            @Valid @RequestBody SignupRequestDto signUpRequestDto) {
        User mappedUser = signUpRequestDomainMapper.mapToDomain(signUpRequestDto);
        User registeredUser = registerUserInBound.registerUser(mappedUser);
        UserInfoResponseDto response = userDtoMapper.mapToDto(registeredUser);
        return ResponseEntity.ok(response);

    }
}






