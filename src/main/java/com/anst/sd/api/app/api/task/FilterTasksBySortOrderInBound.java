package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface FilterTasksBySortOrderInBound {
    List<Task> filterInternal(List<Task> tasks, SortOrder order);
}
