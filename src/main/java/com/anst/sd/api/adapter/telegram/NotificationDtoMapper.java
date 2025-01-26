package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.adapter.telegram.dto.NotificationDto;
import com.anst.sd.api.domain.notification.Notification;
import com.anst.sd.api.domain.notification.NotificationTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationDtoMapper {
    public NotificationDto execute(Notification notification) {
        NotificationTemplate template = notification.getTemplate();
        String body = constructNotificationBody(template, notification.getParams());
        NotificationDto dto = new NotificationDto();
        dto.setBody(body);
        dto.setTitle(template.getTitle());
        dto.setTelegramId(notification.getRecipientTelegramId());
        return dto;
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String constructNotificationBody(NotificationTemplate template, Map<String, String> params) {
        String body = template.getBody();
        if (MapUtils.isEmpty(params)) {
            return body;
        }
        return StringSubstitutor.replace(body, params);
    }
}
