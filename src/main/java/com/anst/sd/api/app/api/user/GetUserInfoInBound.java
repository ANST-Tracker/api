package com.anst.sd.api.app.api.user;

import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponse;

public interface GetUserInfoInBound {
    UserInfoResponse getUserInfo(Long userId);
}
