package com.anst.sd.api.adapter.telegram.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String title;
    private String body;
    private String telegramId;
}
