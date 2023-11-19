package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface GetTasksByUserInBound {
    List<Task> getTasks(Long userId, Integer page);
}
