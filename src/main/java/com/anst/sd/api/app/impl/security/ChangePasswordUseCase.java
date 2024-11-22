package com.anst.sd.api.app.impl.security;

import com.anst.sd.api.adapter.rest.security.dto.UpdatedPasswordDto;
import com.anst.sd.api.app.api.security.ChangePasswordInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.AuthErrorMessages;
import com.anst.sd.api.security.app.api.AuthException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangePasswordUseCase implements ChangePasswordInBound {
    private final UserRepository userRepository;
    private final DeleteSessionDelegate deleteSessionDelegate;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void changePassword(Long userId, UpdatedPasswordDto updatedPasswordDto) {
        User user = userRepository.getById(userId);
        validateOldPassword(user, updatedPasswordDto);
        user.setPassword(encoder.encode(updatedPasswordDto.getNewPassword()));
        userRepository.save(user);
        deleteSessionDelegate.removeAllSessions(user);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateOldPassword(User user, UpdatedPasswordDto updatedPasswordDto) {
        if (!user.getPassword().equals(encoder.encode(updatedPasswordDto.getOldPassword()))) {
            throw new AuthException(AuthErrorMessages.INVALID_PASSWORD);
        }
    }
}
