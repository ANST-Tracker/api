package com.anst.sd.api.app.impl.notification;

import com.anst.sd.api.app.api.notification.GetNotificationsInBound;
import com.anst.sd.api.app.api.notification.NotificationRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.notification.Notification;
import com.anst.sd.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class GetNotificationsUseCase implements GetNotificationsInBound {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<Notification> getNotifications(UUID userId) {
        log.info("Requesting notifications for userId {}", userId);
        User user = userRepository.getById(userId);
        return notificationRepository.findAllByRecipientTelegramId(user.getTelegramId());
    }
}
