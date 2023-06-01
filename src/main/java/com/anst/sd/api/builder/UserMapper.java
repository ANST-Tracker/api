package com.anst.sd.api.builder;

import com.anst.sd.api.model.dto.response.UserInfoResponse;
import com.anst.sd.api.model.entity.User;

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
