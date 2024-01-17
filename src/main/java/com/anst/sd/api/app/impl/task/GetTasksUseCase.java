package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.GetTasksInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTasksUseCase implements GetTasksInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Task> get(Long userId, Long projectId, Integer page) {
        log.info("Get user tasks by userId {}", userId);
        if (page == null || page < 0) {
            page = 0;
        }
        Page<Task> pageResponse = taskRepository.findTasksByUserIdAndProjectId(userId, projectId, page);
        return pageResponse.toList();
    }
}
