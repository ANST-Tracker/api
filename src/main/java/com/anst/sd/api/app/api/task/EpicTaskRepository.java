package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.EpicTask;

public interface EpicTaskRepository {
    EpicTask getBySimpleId(String simpleId);
}
