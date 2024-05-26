package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;

import java.util.UUID;

public interface RegisterUserInBound {
    JwtResponse register(User user, UUID deviceToken);
}
