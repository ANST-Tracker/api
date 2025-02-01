package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.UUID;

public interface UserRepository {
    User getByUsername(String username);

    User getById(UUID id);
}
