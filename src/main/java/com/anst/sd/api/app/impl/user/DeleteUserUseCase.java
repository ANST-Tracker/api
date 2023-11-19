package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.user.DeleteUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.anst.sd.api.security.AuthErrorMessages.USER_DOESNT_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserInBound {
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<User> deleteUser(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_DOESNT_EXISTS);
        }
        userRepository.deleteById(userId);
        return user;
    }
}
