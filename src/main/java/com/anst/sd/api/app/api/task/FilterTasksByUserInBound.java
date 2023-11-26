package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface FilterTasksByUserInBound {
    List<Task> filterTasks(Long userId, FilterRequest filterRequestDto);
}
