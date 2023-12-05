package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface UpdateTaskInBound {
    Task update(Long userId, Task request);
}
