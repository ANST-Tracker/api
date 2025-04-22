package com.anst.sd.api.adapter.rest.notification.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class NotificationInfoDto {
    Boolean viewed;
    String recipientTelegramId;
    Instant creationDateTime;
    String text;
}
