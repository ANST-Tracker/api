package com.anst.sd.api.app.impl.notification;

import com.anst.sd.api.domain.notification.NotificationTemplate;
import com.anst.sd.api.domain.user.User;
import lombok.Data;

import java.util.Map;

@Data
public class NotificationCreatedEvent {
    private User recipient;
    private Map<String, String> params;
    private NotificationTemplate template;
}
