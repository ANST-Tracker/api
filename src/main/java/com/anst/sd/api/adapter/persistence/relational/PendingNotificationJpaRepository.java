package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.notification.PendingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingNotificationJpaRepository extends JpaRepository<PendingNotification, Long> {
}
