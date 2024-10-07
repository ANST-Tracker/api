package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskRepository {
    Page<Task> findTasksByUserIdAndProjectId(Long userId, Long projectId, Integer page);

    Task save(Task task);

    Task findByIdAndUser(Long id, Long userId);

    void deleteById(Long id);

    List<Task> findByFilter(Long userId, TaskFilter filter);

    double maxOrderNumberTasksByUserIdAndProjectId(Long userId,Long projectId);
}
