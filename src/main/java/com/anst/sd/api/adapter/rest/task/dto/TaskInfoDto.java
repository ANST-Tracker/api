package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfoDto {
    @NotNull
    Long id;
    @NotBlank
    String data;
    LocalDateTime deadline;
    @NotNull
    TaskStatus status;
    String description;
    BigDecimal orderNumber;
    List<PendingNotificationDto> pendingNotifications;
}
