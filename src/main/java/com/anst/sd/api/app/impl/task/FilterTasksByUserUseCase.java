package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.FilterRequest;
import com.anst.sd.api.app.api.task.FilterTasksByOrderInBound;
import com.anst.sd.api.app.api.task.FilterTasksByUserInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterTasksByUserUseCase implements FilterTasksByUserInBound {
    private final TaskRepository taskRepository;
    private final FilterTasksByOrderInBound filterTasksByOrderInBound;
    @Value("${pageable.size}")
    private Integer pageSize;

    @Override
    @Transactional(readOnly = true)
    public List<Task> filterTasks(Long userId, FilterRequest filterRequest) {
        log.info("Task filter has been started by userId {}", userId);
        var pageNumber = filterRequest.getPage();
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var statuses = filterRequest.getNecessaryStatuses();
        if (statuses == null || statuses.size() == 0) {
            statuses = List.of(TaskStatus.values());
        }

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("created").descending());
        var tasks = taskRepository.findTasksByUserIdAndStatusIn(userId, statuses, pageRequest);

        return filterTasksByOrderInBound.filter(filterRequest, tasks.stream().toList());
    }
}
