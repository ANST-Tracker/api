package com.anst.sd.api.app.api.notification;

import com.anst.sd.api.domain.notification.Notification;

public interface SendNotificationOutbound {
    void send(Notification notification);
}
