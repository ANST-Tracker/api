package com.anst.sd.api.app.impl.notification;

import com.anst.sd.api.app.api.notification.NotificationRepository;
import com.anst.sd.api.app.api.notification.SendNotificationOutbound;
import com.anst.sd.api.domain.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {
    private final NotificationRepository notificationRepository;
    private final SendNotificationOutbound sendNotificationOutbound;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = {NotificationCreatedEvent.class})
    @Transactional
    public void execute(NotificationCreatedEvent event) {
        Notification notification = new Notification();
        notification
            .setRecipientTelegramId(event.getRecipient().getTelegramId())
            .setRecipientLogin(event.getRecipient().getUsername())
            .setViewed(false)
            .setParams(event.getParams())
            .setTemplate(event.getTemplate());
        notificationRepository.save(notification);
        sendNotificationOutbound.send(notification);
    }
}
