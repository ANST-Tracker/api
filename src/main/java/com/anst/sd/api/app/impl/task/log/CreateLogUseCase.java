package com.anst.sd.api.app.impl.task.log;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.log.CreateLogInBound;
import com.anst.sd.api.app.api.task.log.LogRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.task.Log;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateLogUseCase implements CreateLogInBound {
    private final ProjectRepository projectRepository;
    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final AbstractTaskRepository abstractTaskRepository;

    @Override
    @Transactional
    public Log create(Log createLog, UUID projectId, String taskId, UUID userId) {
        log.info("Creating log in project {} for task {} by user {}", projectId, taskId, userId);
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(userId, projectId));
        }
        createLog
                .setTask(abstractTaskRepository.getByIdAndProjectId(taskId, projectId))
                .setUser(userRepository.getById(userId));
        return logRepository.save(createLog);
    }
}
