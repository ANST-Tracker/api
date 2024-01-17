package com.anst.sd.api.app.api.task;

import com.anst.sd.api.app.api.DateRangeFilter;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskFilter {
    DateRangeFilter deadline;
    List<TaskStatus> status;
    Integer page;
    Long projectId;
}
