package com.anst.sd.api.adapter.rest.notification.dto;

import com.anst.sd.api.domain.notification.NotificationTemplate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class NotificationInfoDto {
    Boolean viewed;
    String recipientLogin;
    String recipientTelegramId;
    Instant creationDateTime;
    NotificationTemplate template;
    Map<String, String> params;
}
