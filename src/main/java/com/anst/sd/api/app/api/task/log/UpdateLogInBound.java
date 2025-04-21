package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.task.Log;

import java.time.LocalDate;
import java.util.UUID;

public interface UpdateLogInBound {
    Log update(UUID id, String comment, TimeEstimation timeEstimation, UUID projectId,
               String taskId, UUID userId, LocalDate date);
}
