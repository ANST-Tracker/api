package com.anst.sd.api.controller;

import com.anst.sd.api.model.entity.User;
import com.anst.sd.api.model.exception.AuthException;
import com.anst.sd.api.model.dto.request.LoginRequest;
import com.anst.sd.api.model.dto.request.RefreshRequest;
import com.anst.sd.api.model.dto.request.SignupRequest;
import com.anst.sd.api.model.dto.response.JwtResponse;
import com.anst.sd.api.model.dto.response.RefreshResponse;
import com.anst.sd.api.model.exception.ClientException;
import com.anst.sd.api.service.AuthService;
import com.anst.sd.api.service.UserService;
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
    private final UserService userService;
    private final AuthService authService;

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
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @Operation(summary = "User authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated successfully",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = JwtResponse.class))
                    )),
            @ApiResponse(
                    responseCode = "409",
                    description = "Invalid username or password",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = AuthException.class))
                    )
            )})
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
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
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(userService.registerUser(signUpRequest));
    }
}






