package com.anst.sd.api.app.api.user;

import com.anst.sd.api.app.api.security.SignupRequest;
import com.anst.sd.api.domain.user.User;

public interface RegisterUserInBound {
    User registerUser(SignupRequest signupRequest);
}
