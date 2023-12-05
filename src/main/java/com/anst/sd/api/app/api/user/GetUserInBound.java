package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

public interface GetUserInBound {
    User get(Long userId);
}
