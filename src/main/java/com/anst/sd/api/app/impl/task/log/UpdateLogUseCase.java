package com.anst.sd.api.app.impl.task.log;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.log.LogRepository;
import com.anst.sd.api.app.api.task.log.UpdateLogInBound;
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
public class UpdateLogUseCase implements UpdateLogInBound {
    private final ProjectRepository projectRepository;
    private final LogRepository logRepository;
    private final TotalLogNotificationGenerator totalLogNotificationGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public Log update(Log updateLog, UUID projectId, String taskId, UUID userId) {
        log.info("Update log {} in project {} for task {} by user {}", updateLog.getId(), projectId, taskId, userId);
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(userId, projectId));
        }
        Log log = logRepository.findByIdAndTaskAndProjectIdAndUserId(
                updateLog.getId(), taskId, projectId, userId);
        boolean doSendNotification = !log.getTimeEstimation().equals(updateLog.getTimeEstimation());
        log
                .setComment(updateLog.getComment())
                .setTimeEstimation(updateLog.getTimeEstimation())
                .setDate(updateLog.getDate());
        log = logRepository.save(log);
        if (doSendNotification) {
            totalLogNotificationGenerator.create(taskId, userId)
                    .forEach(applicationEventPublisher::publishEvent);
        }
        return log;
    }
}
