package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface FilterTasksInBound {
    List<Task> filter(Long userId, TaskFilter taskFilter);
}
