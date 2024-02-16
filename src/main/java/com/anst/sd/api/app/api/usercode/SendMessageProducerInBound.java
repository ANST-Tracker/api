package com.anst.sd.api.app.api.usercode;

import com.anst.sd.api.domain.user.UserCode;

public interface SendMessageProducerInBound {
    void sendMessage(UserCode clazz);
}
