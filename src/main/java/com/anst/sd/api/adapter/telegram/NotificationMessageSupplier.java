package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.adapter.telegram.dto.KafkaMessageMapper;
import com.anst.sd.api.adapter.telegram.dto.NotificationDto;
import com.anst.sd.api.app.api.notification.SendNotificationOutbound;
import com.anst.sd.api.domain.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationMessageSupplier implements SendNotificationOutbound {
    private static final String TOPIC = "API_NOTIFICATION-CREATED_EVENT";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaMessageMapper<NotificationDto> kafkaMessageMapper;
    private final NotificationDtoMapper mapper;

    @Override
    public void send(Notification notification) {
        log.info("Sending notification {}", notification);
        NotificationDto dto = mapper.execute(notification);
        kafkaTemplate.send(TOPIC, kafkaMessageMapper.mapToKafkaMessage(dto));
    }
}
