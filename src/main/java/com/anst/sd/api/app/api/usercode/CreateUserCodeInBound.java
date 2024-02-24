package com.anst.sd.api.app.api.usercode;

import com.anst.sd.api.domain.user.UserCode;

public interface CreateUserCodeInBound {
    UserCode create(String userId, String telegramId);
}
