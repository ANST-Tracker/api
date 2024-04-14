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

import java.time.LocalDateTime;
import java.util.List;

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

    @BeforeEach
    void setUp() {
        useCase = new CreateTaskUseCase(taskRepository, projectRepository);
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void create_successfully() {
        User user = createTestUser();
        Project project = createTestProject(user);
        when(projectRepository.getByIdAndUserId(1L, 1L)).thenReturn(project);
        Task task = createTask();

        Task result = useCase.create(1L, 1L, task);

        assertEquals(TaskStatus.BACKLOG, result.getStatus());
        assertEquals(project.getId(), result.getProject().getId());
        assertEquals(user.getId(), result.getProject().getUser().getId());
        assertEquals(DEADLINE, result.getDeadline());
        assertEquals("testData", result.getData());
        assertEquals("testDescription", result.getDescription());
    }

    private Task createTask() {
        Task task = new Task();
        PendingNotification pendingNotification = new PendingNotification();
        task.setData("testData");
        task.setDescription("testDescription");
        task.setDeadline(DEADLINE);
        pendingNotification.setTask(task);
        pendingNotification.setRemindIn(DEADLINE);
        task.setPendingNotifications(List.of(pendingNotification));
        return task;
    }
}