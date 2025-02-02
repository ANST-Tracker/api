package com.anst.sd.api.adapter.persistence.mongo;

import com.anst.sd.api.domain.notification.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMongoRepository extends MongoRepository<Notification, String> {
}
