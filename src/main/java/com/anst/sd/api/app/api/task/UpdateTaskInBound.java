package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface UpdateTaskInBound {
    Task update(Long userId, Long taskId, Task request, List<Long> tagIds);
}
