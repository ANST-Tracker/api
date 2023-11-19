package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface FilterTasksByOrderInBound {
    List<Task> filter(FilterRequest filterRequest, List<Task> tasks);
}
