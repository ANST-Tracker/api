package com.anst.sd.api.app.api.user;

import com.anst.sd.api.security.app.api.JwtResponse;

public interface LoginUserInBound {
    JwtResponse login(String username, String password);
}
