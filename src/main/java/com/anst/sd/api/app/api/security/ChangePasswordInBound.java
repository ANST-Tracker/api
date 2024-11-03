package com.anst.sd.api.app.api.security;

import com.anst.sd.api.adapter.rest.security.dto.UpdatedPasswordDto;

public interface ChangePasswordInBound {
    void changePassword(Long userId, UpdatedPasswordDto updatedPasswordDto);
}
