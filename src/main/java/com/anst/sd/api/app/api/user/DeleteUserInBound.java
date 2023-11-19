package com.anst.sd.api.app.api.user;

import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponse;

public interface DeleteUserInBound {
    UserInfoResponse deleteUser(Long userId);
}
