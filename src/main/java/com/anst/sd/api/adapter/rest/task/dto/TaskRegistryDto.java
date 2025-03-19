package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.FullCycleStatus;
import com.anst.sd.api.domain.task.TaskType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRegistryDto {
    Integer simpleId;
    String name;
    TaskType type;
    FullCycleStatus status;
    String assigneeFullName;
    String creatorFullName;
}
