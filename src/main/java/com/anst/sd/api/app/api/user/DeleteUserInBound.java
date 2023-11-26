package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.Optional;

public interface DeleteUserInBound {
    Optional<User> deleteUser(Long userId);
}
