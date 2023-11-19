package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.adapter.rest.user.dto.UserInfoResponse;
import com.anst.sd.api.adapter.rest.user.dto.UserMapper;
import com.anst.sd.api.app.api.user.GetUserInfoInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.security.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.anst.sd.api.security.AuthErrorMessages.USER_DOESNT_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserInfoUseCase implements GetUserInfoInBound {
    private final UserRepository userRepository;

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AuthException(USER_DOESNT_EXISTS);
        }
        return UserMapper.toApi(user.get());
    }
}
