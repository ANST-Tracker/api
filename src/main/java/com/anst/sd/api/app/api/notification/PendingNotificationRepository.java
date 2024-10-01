package com.anst.sd.api.app.api.notification;

import com.anst.sd.api.domain.notification.PendingNotification;

import java.util.List;

public interface PendingNotificationRepository {
  List<PendingNotification> findReadyToExecution();

  void deleteAll(List<PendingNotification> pendingNotifications);
}
