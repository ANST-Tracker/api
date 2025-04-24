package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.log.LogNotFound;
import com.anst.sd.api.app.api.task.log.LogRepository;
import com.anst.sd.api.domain.task.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LogRepositoryImpl implements LogRepository {
    private final LogJpaRepository logJpaRepository;

    @Override
    public Log save(Log log) {
        return logJpaRepository.save(log);
    }

    @Override
    public List<Log> findAll(String taskId, UUID projectId) {
        return logJpaRepository.findAllByTaskAndProject(taskId, projectId);
    }

    @Override
    public Log findByIdAndTaskAndProjectIdAndUserId(UUID id, String taskId, UUID projectId, UUID userId) {
        return logJpaRepository.findByIdAndTaskAndProjectAndUser(id, taskId, projectId, userId)
                .orElseThrow(() -> new LogNotFound(id, taskId, projectId, userId));
    }

    @Override
    public List<Log> findAllByPeriodAndProjectAndUser(UUID projectId, UUID userId, LocalDate start, LocalDate end) {
        return logJpaRepository.findAllByPeriodAndProjectAndUser(projectId, userId, start, end);
    }

    @Override
    public List<Log> findAllByPeriodAndUser(UUID userId, LocalDate start, LocalDate end) {
        return logJpaRepository.findAllByPeriodAndUser(userId, start, end);
    }

    @Override
    public void delete(UUID id) {
        logJpaRepository.deleteById(id);
    }

    @Override
    public Boolean existsById(UUID id) {
        return logJpaRepository.existsById(id);
    }
}
