package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskRequest {
    String data;
    LocalDateTime deadline;
    Long id;
    String description;
    TaskStatus status;
}
