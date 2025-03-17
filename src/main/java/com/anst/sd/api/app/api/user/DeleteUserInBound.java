package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.UUID;

public interface DeleteUserInBound {
    User delete(UUID userId, UUID userIdDeleted);
}
