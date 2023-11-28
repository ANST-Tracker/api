package com.anst.sd.api.adapter.rest.user;

import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponseDto;
import com.anst.sd.api.app.api.user.DeleteUserInBound;
import com.anst.sd.api.app.api.user.GetUserInfoInBound;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.AuthException;
import com.anst.sd.api.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final DeleteUserInBound deleteUserInBound;
    private final GetUserInfoInBound getUserInfoInBound;
    private final JwtService jwtService;
    private final UserDtoMapper userDtoMapper;

    @Operation(summary = "Get current user information")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User information received successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username doesn't exists",
                    content = @Content(schema = @Schema(implementation = AuthException.class))
            )
    })
    @GetMapping("/current")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser() {
        User result = getUserInfoInBound.getUserInfo(jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(userDtoMapper.mapToDto(result));
    }

    @Operation(
            summary = "Delete current user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User deleted successfully",
                            useReturnTypeSchema = true)
            })
    @DeleteMapping("/current")
    public ResponseEntity<UserInfoResponseDto> deleteCurrentUser() {
        User result = deleteUserInBound.deleteUser(jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(userDtoMapper.mapToDto(result));
    }
}
