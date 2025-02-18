package com.anst.sd.api.app.api.security;

public interface CheckCodeInbound {
    String check(String telegramId, String code, String username);
}
