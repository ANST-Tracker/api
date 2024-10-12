package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UpdateOrderNumberTaskUseCaseTest extends AbstractUnitTest {
    private static final LocalDateTime DEADLINE = LocalDateTime.now().plusDays(5);

    private UpdateOrderNumberTaskUseCase useCase;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        useCase = new UpdateOrderNumberTaskUseCase(taskRepository);
    }

    @Test
    void updateOrderNumber_successfully() {
        Long userId = 1L;
        Long taskId = 1L;
        BigDecimal newOrderNumber = new BigDecimal("5");
        Task task = createTask();
        when(taskRepository.findByIdAndUser(taskId, userId)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        Task result = useCase.updateOrderNumber(userId, taskId, newOrderNumber);

        assertEquals(newOrderNumber, result.getOrderNumber());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Task createTask() {
        Task task = new Task();
        PendingNotification pendingNotification = createPendingNotification();
        task.setData("testData");
        task.setDescription("testDescription");
        task.setDeadline(DEADLINE);
        task.setOrderNumber(new BigDecimal("1"));
        pendingNotification.setTask(task);
        pendingNotification.setExecutionDate(DEADLINE.toInstant(ZoneOffset.UTC));
        task.setPendingNotifications(List.of(pendingNotification));
        return task;
    }

    private PendingNotification createPendingNotification() {
        PendingNotification pendingNotification = new PendingNotification();
        pendingNotification.setAmount(20);
        pendingNotification.setTimeType(TimeUnit.HOURS);
        return pendingNotification;
    }
}
