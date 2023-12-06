package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateTaskUseCaseTest extends AbstractUnitTest {
    private static final LocalDateTime DEADLINE = LocalDateTime.now().plusDays(5);
    private CreateTaskUseCase useCase;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        useCase = new CreateTaskUseCase(taskRepository, userRepository);
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void create() {
        User user = createTestUser();
        when(userRepository.getById(1L)).thenReturn(user);
        Task task = createTask();

        Task result = useCase.create(1L, task);

        assertNotNull(result.getCreated());
        assertEquals(TaskStatus.BACKLOG, result.getStatus());
        assertEquals(user.getId(), result.getUser().getId());
        assertEquals(DEADLINE, result.getDeadline());
        assertEquals("testData", result.getData());
        assertEquals("testDescription", result.getDescription());
    }

    private Task createTask() {
        Task task = new Task();
        task.setData("testData");
        task.setDescription("testDescription");
        task.setDeadline(DEADLINE);
        return task;
    }

}