package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.util.UUID;

public interface DeleteLogInBound {
    Log delete(UUID id, UUID projectId, String taskId, UUID userId);
}
