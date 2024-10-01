package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;

public interface RegisterUserInBound {
    JwtResponse register(User user);
}
