package com.anst.sd.api.app.impl.task.log;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.log.GetTimeSheetLogsByProjectInBound;
import com.anst.sd.api.app.api.task.log.LogRepository;
import com.anst.sd.api.domain.task.Log;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetTimeSheetLogsByProjectUseCase implements GetTimeSheetLogsByProjectInBound {
    private final ProjectRepository projectRepository;
    private final LogRepository logRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Log> get(UUID projectId, UUID userId, LocalDateTime start, LocalDateTime end) {
        log.info("Getting logs for user {} in project {} from {} to {}", userId, projectId, start.toString(), end.toString());
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(userId, projectId));
        }
        return logRepository.findAllByPeriodAndProjectAndUser(projectId, userId, start, end);
    }
}
