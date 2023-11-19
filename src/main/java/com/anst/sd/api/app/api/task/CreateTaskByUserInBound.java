package com.anst.sd.api.app.api.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.domain.task.Task;

public interface CreateTaskByUserInBound {
    TaskInfo createTask(Long userId, Task task);
}
