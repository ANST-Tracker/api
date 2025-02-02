package com.anst.sd.api.app.api.notification;

import com.anst.sd.api.domain.notification.Notification;

public interface NotificationRepository {
    void save(Notification notification);
}
