package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.DefectTask;

public interface DefectTaskRepository {
    DefectTask getBySimpleId(String simpleId);
}
