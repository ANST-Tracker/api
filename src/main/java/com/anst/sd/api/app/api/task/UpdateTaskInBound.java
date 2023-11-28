package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface UpdateTaskInBound {
    Task updateTask(Long userId, Task request);
}
