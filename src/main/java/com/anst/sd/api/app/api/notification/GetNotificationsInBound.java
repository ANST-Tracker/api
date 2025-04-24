package com.anst.sd.api.app.api.notification;

import com.anst.sd.api.domain.notification.Notification;

import java.util.List;
import java.util.UUID;

public interface GetNotificationsInBound {
    List<Notification> getNotifications(UUID userId);
}
