package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
