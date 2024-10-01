package com.anst.sd.api.app.api.security;

import com.anst.sd.api.domain.security.UserCode;

public interface SendCodeOutbound {
  void send(UserCode userCode);
}
