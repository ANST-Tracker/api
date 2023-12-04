package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface GetTaskInBound {
    Task get(Long userId, Long id);
}
