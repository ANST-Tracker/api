package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.GetTaskInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTaskUseCase implements GetTaskInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Task get(Long userId, Long id) {
        log.info("Getting task by id {} and userId {}", id, userId);
        return taskRepository.findByIdAndUser(id, userId);
    }
}
