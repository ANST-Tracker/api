package com.anst.sd.api.app.api.notification;

import com.anst.sd.api.domain.notification.Notification;

import java.util.List;

public interface NotificationRepository {
    void saveAll(List<Notification> notifications);
}
