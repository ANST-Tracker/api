package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.adapter.telegram.dto.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "telegram-bot", url = "${service.telegram-bot.api.url}")
public interface TelegramBotFeignClient {
    @PostMapping("/api/v1/internal/checking-accounts/bics")
    void sendNotification(@RequestBody NotificationDto notificationDto);
}
