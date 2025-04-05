package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Subtask;

public interface SubtaskRepository {
    Subtask getBySimpleId(String simpleId);
}
