package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface CreateTaskInBound {
    Task create(Long userId, Long projectId, Task task);
}
