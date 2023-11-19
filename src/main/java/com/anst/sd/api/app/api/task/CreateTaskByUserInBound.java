package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface CreateTaskByUserInBound {
    Task createTask(Long userId, Task task);
}
