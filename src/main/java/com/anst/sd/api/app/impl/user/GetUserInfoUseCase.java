package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.user.GetUserInfoInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserInfoUseCase implements GetUserInfoInBound {
    private final UserRepository userRepository;

    @Override
    public User getUserInfo(Long userId) {
        log.info("Getting user info for id {}", userId);
        return userRepository.getById(userId);
    }
}
