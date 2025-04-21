package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.util.UUID;

public interface CreateLogInBound {
    Log create(Log createLog, UUID projectId, String taskId, UUID userId);
}
