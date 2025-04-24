package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.util.UUID;

public interface UpdateLogInBound {
    Log update(Log updateLog, UUID projectId, String taskId, UUID userId);
}
