package com.anst.sd.api.adapter.rest.task.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class PendingNotificationDto {
  int amount;
  TimeUnit timeType;
}
