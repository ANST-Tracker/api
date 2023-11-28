package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

public interface GetUserInfoInBound {
    User getUserInfo(Long userId);
}
