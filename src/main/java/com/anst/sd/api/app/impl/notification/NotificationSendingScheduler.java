package com.anst.sd.api.app.impl.notification;

import com.anst.sd.api.app.api.notification.NotificationRepository;
import com.anst.sd.api.app.api.notification.PendingNotificationRepository;
import com.anst.sd.api.app.api.notification.SendNotificationOutbound;
import com.anst.sd.api.domain.notification.Notification;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.anst.sd.api.domain.project.ProjectType.BUCKET;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSendingScheduler {
  private final PendingNotificationRepository pendingNotificationRepository;
  private final NotificationRepository notificationRepository;
  private final SendNotificationOutbound sendNotificationOutbound;

  @Transactional
  @Scheduled(fixedDelayString = "${shedlock.notification-sending.execution-delay}")
  @SchedulerLock(name = "notificationSendingTask",
          lockAtLeastFor = "${shedlock.notification-sending.lock-at-least-for}",
          lockAtMostFor = "${shedlock.notification-sending.lock-at-most-for}")
  public void execute() {
    LockAssert.assertLocked();
    List<PendingNotification> pendingNotifications = pendingNotificationRepository.findReadyToExecution();
    if (CollectionUtils.isEmpty(pendingNotifications)) {
      return;
    }
    log.info("Executing {} pending notifications", pendingNotifications.size());
    List<Notification> archiveNotifications = new ArrayList<>();
    pendingNotifications.forEach(notification -> {
      try {
        sendNotificationOutbound.send(notification);
      } catch (Exception e) {
        log.warn("Failed to sent notification for task {}", notification.getTask().getId());
      }
      Notification archiveNotification = createNotification(notification);
      archiveNotifications.add(archiveNotification);
    });
    notificationRepository.saveAll(archiveNotifications);
    pendingNotificationRepository.deleteAll(pendingNotifications);
  }

  // ===================================================================================================================
  // = Implementation
  // ===================================================================================================================

  private Notification createNotification(PendingNotification pendingNotification) {
    Notification notification = new Notification();
    notification.setExecutionDate(Instant.now());
    notification.setTaskId(pendingNotification.getTask().getId());
    notification.setTaskName(pendingNotification.getTask().getData());
    Project project = pendingNotification.getTask().getProject();
    if (!BUCKET.equals(project.getProjectType())) {
      notification.setProjectName(project.getName());
    }
    return notification;
  }
}
