package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskRepository {
    Page<Task> findTasksByUserId(Long userId, Pageable page);

    Page<Task> findTasksByUserIdAndStatusIn(Long userId, List<TaskStatus> status, Pageable page);

    Task save(Task task);

    Task findByIdAndUser(Long id, Long userId);

    void deleteById(Long id);

}
