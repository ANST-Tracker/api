package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.app.api.DateRangeFilter;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskFilterRequestDto {
    DateRangeFilter deadline;
    List<TaskStatus> status;
    Integer page;
}
