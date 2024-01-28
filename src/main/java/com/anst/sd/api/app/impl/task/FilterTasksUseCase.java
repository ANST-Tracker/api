package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.FilterTasksInBound;
import com.anst.sd.api.app.api.task.TaskFilter;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterTasksUseCase implements FilterTasksInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Task> filter(Long userId, TaskFilter filter) {
        log.info("Filtering task for user {} with filter {}", userId, filter);
        if (filter.getPage() == null || filter.getPage() < 0) {
            filter.setPage(0);
        }
        return taskRepository.findByFilter(userId, filter);
    }
}
