package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.GetUserInTaskInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.anst.sd.api.security.AuthErrorMessages.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserInTaskUseCase implements GetUserInTaskInBound {
    private final UserRepository userRepository;

    @Override
    public User getUser(Long userId) {
        log.info("Get user by userId {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("User not found with userId {}", userId);
            throw new AuthException(USER_NOT_FOUND);
        }
        log.debug("Got user by userId {}", user);
        return user.get();
    }
}
