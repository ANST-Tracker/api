package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.GetTasksByMonthAndYearInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTasksByMonthAndYearUseCase implements GetTasksByMonthAndYearInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<LocalDate, List<Task>> getTasksByMonthAndYear(Long userId, Integer month, Integer year) {
        LocalDateTime  startDate = LocalDateTime.of(year, month, 1,0,0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        List<Task> tasks = taskRepository.findTasksByUserIdAndDateRange(userId, startDate, endDate);

        return tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getDeadline().toLocalDate()));
    }
}