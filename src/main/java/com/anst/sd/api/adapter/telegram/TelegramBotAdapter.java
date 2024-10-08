package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.adapter.telegram.dto.NotificationDto;
import com.anst.sd.api.adapter.telegram.dto.NotificationDtoMapper;
import com.anst.sd.api.app.api.ServiceUnavailableException;
import com.anst.sd.api.app.api.notification.SendNotificationOutbound;
import com.anst.sd.api.domain.notification.PendingNotification;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBotAdapter implements SendNotificationOutbound {
    private final TelegramBotFeignClient telegramBotFeignClient;
    private final NotificationDtoMapper notificationDtoMapper;

    @Override
    public void send(PendingNotification pendingNotification) {
        log.info("Sending notification request to telegram bot service. Task {} Project {}",
                pendingNotification.getTask().getId(), pendingNotification.getTask().getProject().getId());
        try {
            NotificationDto requestDto = notificationDtoMapper.mapToDto(pendingNotification);
            telegramBotFeignClient.sendNotification(requestDto);
        } catch (FeignException e) {
            log.error("Error occurred while sending notification request", e);
            throw new ServiceUnavailableException(e);
        }
    }
}
