package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.TaskStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskInfoDto {
    Long id;
    String data;
    LocalDateTime deadline;
    TaskStatus status;
    String description;
}
