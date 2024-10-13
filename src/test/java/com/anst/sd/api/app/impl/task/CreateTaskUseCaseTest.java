package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateTaskUseCaseTest extends AbstractUnitTest {
    private static final LocalDateTime DEADLINE = LocalDateTime.now().plusDays(5);

    private CreateTaskUseCase useCase;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private DateConverterDelegate dateConverterDelegate;

    @BeforeEach
    void setUp() {
        useCase = new CreateTaskUseCase(taskRepository, projectRepository, dateConverterDelegate);
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(taskRepository.newOrderNumberTask()).thenReturn(BigDecimal.ONE);
    }

    @Test
    void create_successfully() {
        User user = createTestUser();
        Project project = createTestProject(user);
        when(projectRepository.getByIdAndUserId(1L, 1L)).thenReturn(project);
        Task task = createTask();
        when(dateConverterDelegate.convertToInstant(any(LocalDateTime.class), any(PendingNotification.class)))
                .thenReturn(task.getPendingNotifications().get(0));

        Task result = useCase.create(1L, 1L, task);

        assertEquals(TaskStatus.BACKLOG, result.getStatus());
        assertEquals(project.getId(), result.getProject().getId());
        assertEquals(user.getId(), result.getProject().getUser().getId());
        assertEquals(DEADLINE, result.getDeadline());
        assertEquals(task.getOrderNumber(), result.getOrderNumber());
        assertEquals("testData", result.getData());
        assertEquals("testDescription", result.getDescription());
    }



    private Task createTask() {
        Task task = new Task();
        PendingNotification pendingNotification = createPendingNotification();
        task.setData("testData");
        task.setDescription("testDescription");
        task.setDeadline(DEADLINE);
        task.setOrderNumber(BigDecimal.ONE);
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