package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.Optional;

public interface GetUserInfoInBound {
    Optional<User> getUserInfo(Long userId);
}
