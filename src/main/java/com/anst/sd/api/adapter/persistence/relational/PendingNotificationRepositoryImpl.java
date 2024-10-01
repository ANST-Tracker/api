package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.notification.PendingNotificationRepository;
import com.anst.sd.api.domain.notification.PendingNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PendingNotificationRepositoryImpl implements PendingNotificationRepository {
  private final PendingNotificationJpaRepository pendingNotificationJpaRepository;

  @Override
  public List<PendingNotification> findReadyToExecution() {
    return pendingNotificationJpaRepository.findTop100ByExecutionDateBefore(Instant.now());
  }

  @Override
  public void deleteAll(List<PendingNotification> pendingNotifications) {
    pendingNotificationJpaRepository.deleteAll(pendingNotifications);
  }
}
