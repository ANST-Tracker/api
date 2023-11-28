package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface CreateTaskInBound {
    Task createTask(Long userId, Task task);
}
