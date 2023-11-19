package com.anst.sd.api.app.api.user;

import com.anst.sd.api.app.api.security.JwtResponse;
import com.anst.sd.api.app.api.security.LoginRequest;

public interface LoginUserInBound {
    JwtResponse loginUser(LoginRequest loginRequest);
}
