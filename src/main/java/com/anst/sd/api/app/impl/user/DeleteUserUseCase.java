package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.user.DeleteUserInBound;
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
public class DeleteUserUseCase implements DeleteUserInBound {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public User delete(UUID userId, UUID deletedBy) {
        log.info("Deleting user with id {}, deleted by userId{}", userId, deletedBy);
        var user = userRepository.getById(userId);
        userRepository.deleteById(userId);
        return user;
    }
}
