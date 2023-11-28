package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.TaskStatus;
import jakarta.validation.constraints.NotNull;
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
public class UpdateTaskRequestDto {
    @NotNull
    String data;
    LocalDateTime deadline;
    @NotNull
    Long id;
    String description;
    @NotNull
    TaskStatus status;
}
