package com.anst.sd.api.app.api.user;

import com.anst.sd.api.adapter.rest.dto.SignupRequest;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponse;

public interface RegisterUserInBound {
    UserInfoResponse registerUser(SignupRequest signupRequest);
}
