package com.anst.sd.api.app.api.notification;

import com.anst.sd.api.domain.notification.PendingNotification;

public interface SendNotificationOutbound {
  void send(PendingNotification pendingNotification);
}
