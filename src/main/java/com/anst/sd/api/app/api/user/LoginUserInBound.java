package com.anst.sd.api.app.api.user;

import com.anst.sd.api.security.JwtResponse;

import java.util.UUID;

public interface LoginUserInBound {
    JwtResponse loginUser(String username, String password, UUID deviceToken);
}
