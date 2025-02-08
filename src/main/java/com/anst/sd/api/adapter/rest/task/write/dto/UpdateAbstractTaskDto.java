package com.anst.sd.api.adapter.rest.task.write.dto;

import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.task.TaskPriority;
import com.anst.sd.api.domain.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAbstractTaskDto {
    String name;
    String description;
    TaskType type;
    TaskPriority priority;
    Integer storyPoints;
    UUID assigneeId;
    UUID reviewerId;
    LocalDate dueDate;
    TimeEstimation timeEstimation;
}
