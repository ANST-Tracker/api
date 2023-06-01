package com.anst.sd.api.model.dto.response;

import com.anst.sd.api.model.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskInfo {
    Long id;
    String data;
    LocalDateTime deadline;
    TaskStatus status;
    String description;
}
