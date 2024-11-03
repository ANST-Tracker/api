package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface GetTasksByMonthAndYearInBound {
    Map<LocalDate, List<Task>> getTasksByMonthAndYear(Long userId, Integer month, Integer year);
}