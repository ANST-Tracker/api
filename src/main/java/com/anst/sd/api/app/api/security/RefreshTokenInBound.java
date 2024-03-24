package com.anst.sd.api.app.api.security;

import com.anst.sd.api.security.app.api.JwtResponse;

public interface RefreshTokenInBound {
    JwtResponse refresh(String refreshToken);
}
