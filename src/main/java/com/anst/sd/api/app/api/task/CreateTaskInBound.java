package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface CreateTaskInBound {
    Task create(Long userId, Long projectId, Task task, List<Long> tagIds);

    void create(String userId, String name);
}
