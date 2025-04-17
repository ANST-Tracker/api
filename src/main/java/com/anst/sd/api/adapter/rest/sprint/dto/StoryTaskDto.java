package com.anst.sd.api.adapter.rest.sprint.dto;

import com.anst.sd.api.domain.task.TaskPriority;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryTaskDto {
    String simpleId;
    String name;
    TaskStatus status;
    TaskPriority priority;
    UUID assigneeId;
    List<SubtaskDto> subtasks;
}
