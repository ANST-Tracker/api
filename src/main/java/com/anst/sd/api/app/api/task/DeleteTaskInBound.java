package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface DeleteTaskInBound {
    Task deleteTask(Long userId, Long id);
}
