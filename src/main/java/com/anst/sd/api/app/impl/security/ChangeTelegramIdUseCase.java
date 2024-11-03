package com.anst.sd.api.app.impl.security;

import com.anst.sd.api.adapter.rest.security.dto.UpdatedTelegramDto;
import com.anst.sd.api.app.api.security.ChangeTelegramIdInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.AuthErrorMessages;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeTelegramIdUseCase implements ChangeTelegramIdInBound {
    private final UserRepository userRepository;
    private final DeleteSessionDelegate deleteSessionDelegate;

    @Override
    public void changeTelegramId(Long userId, UpdatedTelegramDto updatedTelegramDto) {
        User user = userRepository.getById(userId);
        validateThatTelegramIdIsNotExists(updatedTelegramDto);
        user.setTelegramId(updatedTelegramDto.getNewTelegramId());
        userRepository.save(user);
        deleteSessionDelegate.removeAllSessions(user);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateThatTelegramIdIsNotExists(UpdatedTelegramDto updatedTelegramDto) {
        if (userRepository.existsByTelegramId(updatedTelegramDto.getNewTelegramId())) {
            throw new AuthException(AuthErrorMessages.TELEGRAM_ID_ALREADY_USED);
        }
    }
}
