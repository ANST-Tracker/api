package com.anst.sd.api.adapter.rest.task.write.dto;

import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.task.TaskPriority;
import com.anst.sd.api.domain.task.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAbstractTaskDto {
    @NotBlank
    String name;
    String description;
    @NotNull
    TaskType type;
    @NotNull
    TaskPriority priority;
    @NotNull
    Integer storyPoints;
    UUID sprintId;
    UUID assigneeId;
    UUID reviewerId;
    UUID storyTaskId;
    UUID epicTaskId;
    UUID projectId;
    LocalDate dueDate;
    TimeEstimation timeEstimation;
    List<UUID> tagIds;
}
