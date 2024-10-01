package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.util.List;

public interface GetTasksInBound {
  List<Task> get(Long userId, Long projectId, Integer page);
}
