package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.Optional;

public interface DeleteTaskByUserInBound {
    Optional<Task> deleteTask(Long userId, Long id);
}
