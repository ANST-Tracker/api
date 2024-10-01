package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.notification.PendingNotification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PendingNotificationJpaRepository extends JpaRepository<PendingNotification, Long> {
  @EntityGraph("pending-notification-full")
  List<PendingNotification> findTop100ByExecutionDateBefore(Instant date);
}
