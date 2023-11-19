package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.user.GetUserInfoInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.anst.sd.api.security.AuthErrorMessages.USER_DOESNT_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserInfoUseCase implements GetUserInfoInBound {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserInfo(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_DOESNT_EXISTS);
        }
        return user;
    }
}
