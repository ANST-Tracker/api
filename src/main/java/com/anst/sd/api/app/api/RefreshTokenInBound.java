package com.anst.sd.api.app.api;

import com.anst.sd.api.adapter.rest.dto.RefreshRequest;
import com.anst.sd.api.security.RefreshResponse;

public interface RefreshTokenInBound {
    RefreshResponse refresh(RefreshRequest request);
}
