package com.anst.sd.api.adapter.rest.user;

import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.app.api.user.GetUserInBound;
import com.anst.sd.api.app.api.user.GetUsersAutocompleteInbound;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "UserController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final GetUserInBound getUserInBound;
    private final GetUsersAutocompleteInbound getUsersAutocompleteInbound;
    private final JwtService jwtService;
    private final UserDtoMapper userDtoMapper;

    @Operation(
            summary = "Get current user information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User information received successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/current")
    public UserInfoDto getUser() {
        User result = getUserInBound.get(jwtService.getJwtAuth().getUserId());
        return userDtoMapper.mapToDto(result);
    }

    @Operation(
            summary = "Get users autocomplete",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/autocomplete")
    public List<UserInfoDto> getUsersAutocomplete(@RequestParam String nameFragment) {
        List<User> result = getUsersAutocompleteInbound.get(nameFragment);
        return result.stream()
                .map(userDtoMapper::mapToDto)
                .toList();
    }
}