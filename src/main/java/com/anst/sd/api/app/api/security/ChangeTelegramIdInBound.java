package com.anst.sd.api.app.api.security;

import com.anst.sd.api.adapter.rest.security.dto.UpdatedTelegramDto;

public interface ChangeTelegramIdInBound {
    void changeTelegramId(Long userId, UpdatedTelegramDto updatedTelegramDto);
}
