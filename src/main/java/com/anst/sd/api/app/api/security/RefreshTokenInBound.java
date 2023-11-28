package com.anst.sd.api.app.api.security;

import com.anst.sd.api.security.RefreshResponse;

public interface RefreshTokenInBound {
    RefreshResponse refresh(String refreshToken);
}
