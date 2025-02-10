package com.anst.sd.api.app.api.task;

import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskStatusDto;
import com.anst.sd.api.domain.task.AbstractTask;

import java.util.UUID;

public interface UpdateAbstractTaskStatusInBound {
    AbstractTask updateStatus(UUID userId, UUID taskId, UpdateAbstractTaskStatusDto request);
}
