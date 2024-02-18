package com.anst.sd.api.app.api.usercode;

import com.anst.sd.api.domain.user.UserCode;

public interface CreateUserCodeOutBound {
    void sendMessage(UserCode clazz);
}
