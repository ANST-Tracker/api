package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.GetTasksInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTasksUseCase implements GetTasksInBound {
    private final TaskRepository taskRepository;
    @Value("${pageable.size}")
    private Integer pageSize;

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasks(Long userId, Integer page) {
        log.info("Get user tasks by userId {}", userId);
        if (page == null || page < 0) {
            page = 0;
        }
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("created").descending());
        Page<Task> pageResponse = taskRepository.findTasksByUserId(userId, pageRequest);
        return pageResponse.stream()
                .sorted(Comparator.comparingInt(a -> TaskStatus.getPriority(a.getStatus())))
                .toList();
    }
}
