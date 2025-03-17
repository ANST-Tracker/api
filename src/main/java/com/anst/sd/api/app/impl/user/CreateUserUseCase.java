package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.user.CreateUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserInBound {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public User create(User user, UUID userIdCreated) {
        log.info("Creating user with id {}, created by userId{}", user.getUsername(), userIdCreated);
        return userRepository.save(user);
    }
}
