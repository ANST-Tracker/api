package com.anst.sd.api.app.api.usercode;

import com.anst.sd.api.domain.user.UserCode;

public interface SendUserCodeProducerOutBound {
    void sendMessage(UserCode clazz);
}
