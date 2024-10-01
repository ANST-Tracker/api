package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.user.GetUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserUseCase implements GetUserInBound {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User get(Long userId) {
        log.info("Getting user info for id {}", userId);
        return userRepository.getById(userId);
    }
}
