package com.anst.sd.api.app.api.usercode;

import com.anst.sd.api.domain.user.UserCode;

public interface SendUserCodeInBound {
    UserCode create(String userId, String telegramId);
}
