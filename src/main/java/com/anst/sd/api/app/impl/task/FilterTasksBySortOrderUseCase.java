package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.FilterTasksBySortOrderInBound;
import com.anst.sd.api.app.api.task.SortOrder;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterTasksBySortOrderUseCase implements FilterTasksBySortOrderInBound {
    @Override
    public List<Task> filterInternal(List<Task> tasks, SortOrder order) {
        log.info("Tasks filter with native order has been started");
        switch (order) {
            case ALPHABET -> {
                log.debug("Tasks has been sorted by alphabet order");
                return tasks
                        .stream()
                        .sorted(Comparator.comparing(Task::getData)).toList();
            }
            case DEADLINE -> {
                log.debug("Tasks has been sorted by deadline order");
                return tasks
                        .stream()
                        .sorted(Comparator.comparing(Task::getDeadline,
                                Comparator.nullsLast(Comparator.naturalOrder()))).toList();
            }
            case COMPLETED -> {
                log.debug("Tasks has been sorted by completed order");
                return tasks.stream()
                        .sorted(Comparator.comparingInt(a -> TaskStatus.getPriority(a.getStatus())))
                        .toList();
            }
            case ALPHABET_DESC -> {
                log.debug("Tasks has been sorted by alphabet desc order");
                return tasks
                        .stream()
                        .sorted(Comparator.comparing(Task::getData).reversed()).toList();
            }
            case DEADLINE_DESC -> {
                log.debug("Tasks has been sorted by deadline desc order");
                return tasks
                        .stream()
                        .sorted(Comparator
                                .comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder()))
                                .reversed())
                        .toList();
            }
            case COMPLETED_DESC -> {
                log.debug("Tasks has been sorted by completed desc order");
                return tasks.stream()
                        .sorted((a, b) -> Integer.compare(
                                TaskStatus.getPriority(b.getStatus()),
                                TaskStatus.getPriority(a.getStatus())
                        )).toList();
            }
        }
        return new ArrayList<>();
    }
}
