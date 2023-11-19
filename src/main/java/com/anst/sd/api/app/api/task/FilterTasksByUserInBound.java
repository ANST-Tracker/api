package com.anst.sd.api.app.api.task;

import com.anst.sd.api.adapter.rest.task.dto.FilterRequest;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;

import java.util.List;

public interface FilterTasksByUserInBound {
    List<TaskInfo> filterTasks(Long userId, FilterRequest filterRequest);
}
