package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.notification.NotificationRepository;
import com.anst.sd.api.domain.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
  private final NotificationJpaRepository notificationJpaRepository;

  @Override
  public void saveAll(List<Notification> notifications) {
    notificationJpaRepository.saveAll(notifications);
  }
}
