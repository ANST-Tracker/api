package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface UpdateTaskByUserInBound {
    Task updateTask(Long userId, Task request);
}
