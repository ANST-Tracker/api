package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.app.api.task.UpdateTaskInBound;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTaskUseCase implements UpdateTaskInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task update(Long userId, Long taskId, Task updated) {
        log.info("Update task by userId {}", userId);
        Task task = taskRepository.findByIdAndUser(taskId, userId);
        mergeTask(task, updated);
        return taskRepository.save(task);
    }

    private void mergeTask(Task original, Task updated) {
        original.setData(updated.getData());
        original.setDeadline(updated.getDeadline());
        original.setDescription(updated.getDescription());
        original.setStatus(updated.getStatus());
    }
}
