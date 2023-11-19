package com.anst.sd.api.app.api.task;

import com.anst.sd.api.adapter.rest.task.dto.FilterRequest;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface FilterTasksByOrderInBound {
    List<TaskInfo> filter(FilterRequest filterRequest, List<Task> tasks);
}
