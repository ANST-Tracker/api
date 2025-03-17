package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.SimpleDictionary;

import java.util.List;

public interface GetAvailableStatusesInBound {
    List<SimpleDictionary> getAppropriateStatuses(String simpleId);
}
