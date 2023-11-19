package com.anst.sd.api.app.api.user;

import com.anst.sd.api.adapter.rest.dto.JwtResponse;
import com.anst.sd.api.adapter.rest.dto.LoginRequest;

public interface LoginUserInBound {
    JwtResponse loginUser(LoginRequest loginRequest);
}
