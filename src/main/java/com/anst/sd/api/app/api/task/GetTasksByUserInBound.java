package com.anst.sd.api.app.api.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;

import java.util.List;

public interface GetTasksByUserInBound {
    List<TaskInfo> getTasks(Long userId, Integer page);
}
