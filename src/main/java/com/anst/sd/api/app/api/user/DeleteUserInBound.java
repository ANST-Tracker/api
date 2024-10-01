package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

public interface DeleteUserInBound {
    User delete(Long userId);
}
