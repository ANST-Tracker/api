package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.UUID;

public interface GetUserInBound {
    User get(UUID userId);
}