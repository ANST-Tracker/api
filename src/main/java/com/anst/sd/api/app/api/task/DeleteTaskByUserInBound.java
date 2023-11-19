package com.anst.sd.api.app.api.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;

public interface DeleteTaskByUserInBound {
    TaskInfo deleteTask(Long userId, Long id);
}
