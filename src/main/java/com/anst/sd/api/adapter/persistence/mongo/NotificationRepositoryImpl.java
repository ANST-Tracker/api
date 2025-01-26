package com.anst.sd.api.adapter.persistence.mongo;

import com.anst.sd.api.app.api.notification.NotificationRepository;
import com.anst.sd.api.domain.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationMongoRepository repository;

    @Override
    public void save(Notification notification) {
        repository.save(notification);
    }
}
