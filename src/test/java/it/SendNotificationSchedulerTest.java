package it;

import com.anst.sd.api.adapter.telegram.dto.NotificationDto;
import com.anst.sd.api.domain.notification.Notification;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class SendNotificationSchedulerTest extends AbstractIntegrationTest {
    private Task task;
    @Captor
    private ArgumentCaptor<NotificationDto> notificationDtoArgumentCaptor;

    @BeforeEach
    void setUp() {
        user = createTestUser();
        project = createProject(user);
        task = createTask(project, null);
    }

    @Test
    void execute_successfully() {
        PendingNotification toBeIgnoredNotification = createNotification(Instant.now().plusSeconds(60), task);
        PendingNotification toBeSentNotification = createNotification(Instant.now().minusSeconds(60), task);

        await().until(() -> notificationJpaRepository.count() != 0);

        assertEquals(toBeIgnoredNotification.getId(), pendingNotificationJpaRepository.findAll().get(0).getId());
        assertEquals(1, pendingNotificationJpaRepository.count());
        assertNotification(toBeSentNotification);
        assertDto();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void assertNotification(PendingNotification pendingNotification) {
        assertEquals(1, notificationJpaRepository.count());
        Notification notification = notificationJpaRepository.findAll().get(0);
        assertEquals(pendingNotification.getTask().getId(), notification.getTaskId());
        assertEquals(pendingNotification.getTask().getData(), notification.getTaskName());
        assertEquals(pendingNotification.getTask().getProject().getName(), notification.getProjectName());
    }

    private void assertDto() {
        verify(telegramBotFeignClient).sendNotification(notificationDtoArgumentCaptor.capture());
        NotificationDto dto = notificationDtoArgumentCaptor.getValue();
        assertEquals(task.getDeadline(), dto.getDeadline());
        assertEquals(user.getTelegramId(), dto.getTelegramId());
        assertEquals(project.getName(), dto.getProjectName());
        assertEquals(task.getData(), dto.getTaskName());
    }

    private PendingNotification createNotification(Instant remindIn, Task task) {
        PendingNotification pendingNotification = new PendingNotification();
        pendingNotification.setTask(task);
        pendingNotification.setExecutionDate(remindIn);
        pendingNotification.setTimeType(TimeUnit.HOURS);
        pendingNotification.setAmount(5);
        return pendingNotificationJpaRepository.save(pendingNotification);
    }
}
