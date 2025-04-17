package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LogRepository {
    Log save(Log comment);

    List<Log> findAll(String taskId, UUID projectId);

    Log findByIdAndTaskAndProjectIdAndUserId(UUID id, String taskId, UUID projectId, UUID userId);

    List<Log> findAllByPeriodAndProjectAndUser(UUID projectId, UUID userId, LocalDateTime start, LocalDateTime end);

    List<Log> findAllByPeriodAndUser(UUID userId, LocalDateTime start, LocalDateTime end);

    void delete(Log log);
}
