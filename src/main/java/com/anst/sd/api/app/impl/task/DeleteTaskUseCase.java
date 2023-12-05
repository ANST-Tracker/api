package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.DeleteTaskInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteTaskUseCase implements DeleteTaskInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task delete(Long userId, Long id) {
        log.info("Delete task with userId {}", userId);
        Task task = taskRepository.findByIdAndUser(id, userId);
        taskRepository.deleteById(id);
        return task;
    }
}
