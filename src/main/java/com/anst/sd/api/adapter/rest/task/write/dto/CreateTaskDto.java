package com.anst.sd.api.adapter.rest.task.write.dto;

import com.anst.sd.api.adapter.rest.task.dto.PendingNotificationDto;
import com.anst.sd.api.domain.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskDto {
    @NotBlank
    String data;
    LocalDateTime deadline;
    String description;
    @NotNull
    TaskStatus status;
    List<PendingNotificationDto> pendingNotifications;
}
