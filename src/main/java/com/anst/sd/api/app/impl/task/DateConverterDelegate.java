package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.domain.notification.PendingNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class DateConverterDelegate {
  public PendingNotification convertToInstant(LocalDateTime deadline, PendingNotification pendingNotification) {
    Instant deadlineInstant = deadline.toInstant(ZoneOffset.UTC);
    Instant executionInstant = deadlineInstant.minus(
            pendingNotification.getAmount(),
            pendingNotification.getTimeType().toChronoUnit());
    pendingNotification.setExecutionDate(executionInstant);
    return pendingNotification;
  }
}