package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.FilterRequest;
import com.anst.sd.api.app.api.task.FilterTasksInBound;
import com.anst.sd.api.app.api.task.SortOrder;
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

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterTasksUseCase implements FilterTasksInBound {
    private final TaskRepository taskRepository;
    @Value("${pageable.size}")
    private Integer pageSize;

    @Override
    @Transactional(readOnly = true)
    public List<Task> filter(Long userId, FilterRequest filterRequest) {
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

        return filter(filterRequest, tasks.stream().toList());
    }

    private List<Task> filter(FilterRequest filterRequest, List<Task> tasks) {
        log.info("Task filter process started");
        if (filterRequest.getOrders() != null) {
            for (var el : filterRequest.getOrders()) {
                tasks = filterInternal(tasks, el);
            }
        }
        return tasks;
    }

    private List<Task> filterInternal(List<Task> tasks, SortOrder order) {
        log.info("Tasks filter with native order has been started");
        return switch (order) {
            case ALPHABET -> tasks.stream()
                    .sorted(Comparator.comparing(Task::getData))
                    .toList();
            case DEADLINE -> tasks.stream()
                    .sorted(Comparator.comparing(Task::getDeadline,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();
            case COMPLETED -> tasks.stream()
                    .sorted(Comparator.comparingInt(a -> TaskStatus.getPriority(a.getStatus())))
                    .toList();
            case ALPHABET_DESC -> tasks
                    .stream()
                    .sorted(Comparator.comparing(Task::getData).reversed())
                    .toList();
            case DEADLINE_DESC -> tasks
                    .stream()
                    .sorted(Comparator
                            .comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder()))
                            .reversed())
                    .toList();
            case COMPLETED_DESC -> tasks.stream()
                    .sorted((a, b) -> Integer.compare(
                            TaskStatus.getPriority(b.getStatus()),
                            TaskStatus.getPriority(a.getStatus())
                    )).toList();
        };
    }
}
