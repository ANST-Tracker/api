package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LogRepository {
    Log save(Log comment);

    List<Log> findAll(String taskId, UUID projectId);

    Log findByIdAndTaskAndProjectIdAndUserId(UUID id, String taskId, UUID projectId, UUID userId);

    List<Log> findAllByPeriodAndProjectAndUser(UUID projectId, UUID userId, LocalDate start, LocalDate end);

    List<Log> findAllByPeriodAndUser(UUID userId, LocalDate start, LocalDate end);

    void delete(UUID id);

    Boolean existsById(UUID id);
}
