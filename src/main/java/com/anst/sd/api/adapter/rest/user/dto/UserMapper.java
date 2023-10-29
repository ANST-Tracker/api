package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.domain.user.User;

public class UserMapper {
    public static UserInfoResponse toApi(User user) {
        return UserInfoResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
