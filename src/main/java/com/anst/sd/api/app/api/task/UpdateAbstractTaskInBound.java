package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.AbstractTask;

import java.util.UUID;

public interface UpdateAbstractTaskInBound {
    AbstractTask update(UUID userId, UUID taskId, AbstractTask request);
}
