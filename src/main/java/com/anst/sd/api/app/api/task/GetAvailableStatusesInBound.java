package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.SimpleDictionary;

import java.util.List;
import java.util.UUID;

public interface GetAvailableStatusesInBound {
    List<SimpleDictionary> getAppropriateStatuses(UUID taskId);
}
