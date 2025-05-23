package com.anst.sd.api.app.impl.task.log;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.log.DeleteLogInBound;
import com.anst.sd.api.app.api.task.log.LogNotFound;
import com.anst.sd.api.app.api.task.log.LogRepository;
import com.anst.sd.api.domain.task.Log;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteLogUseCase implements DeleteLogInBound {
    private final ProjectRepository projectRepository;
    private final LogRepository logRepository;
    private final TotalLogNotificationGenerator totalLogNotificationGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    @Transactional
    public Log delete(UUID id, UUID projectId, String taskId, UUID userId) {
        log.info("Delete log {} in project {} for task {} by user {}",
                id, projectId, taskId, userId);
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(userId, projectId));
        }
        Log log = logRepository.findByIdAndTaskAndProjectIdAndUserId(
                id, taskId, projectId, userId);
        if  (Boolean.FALSE.equals(logRepository.existsById(log.getId()))) {
            throw new LogNotFound(id, taskId, projectId, userId);
        }
        logRepository.delete(log.getId());
        totalLogNotificationGenerator.create(taskId, userId)
                .forEach(applicationEventPublisher::publishEvent);
        return log;
    }
}
