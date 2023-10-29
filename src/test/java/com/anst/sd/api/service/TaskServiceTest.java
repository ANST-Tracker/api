package com.anst.sd.api.service;

import com.anst.sd.api.adapter.rest.task.dto.TaskMapper;
import com.anst.sd.api.app.api.AuthErrorMessages;
import com.anst.sd.api.app.api.ClientErrorMessages;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.app.impl.task.TaskService;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.app.api.AuthException;
import com.anst.sd.api.app.api.ClientException;
import com.anst.sd.api.adapter.persistence.TaskJpaRepository;
import com.anst.sd.api.adapter.persistence.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "anton", roles = "USER")
class TaskServiceTest {
    @Mock
    public TaskJpaRepository taskJpaRepository;
    @Mock
    public UserJpaRepository userJpaRepository;
    @InjectMocks
    public TaskService taskService;
//    @Value("${pageable.size}")
//    private Integer pageSize;
//
//    @BeforeEach
//    void setSettings() {
//        pageSize = 20;
//    }

    public Task createTaskByDefault() {
        return Task.builder()
                .id(1L)
                .description("Join in Tinkoff")
                .data("Pass an interview")
                .user(createUserByDefault())
                .build();
    }

    public User createUserByDefault() {
        return User.builder()
                .id(1L)
                .firstName("Anton")
                .lastName("Pestrikov")
                .username("eridium")
                .email("pestrikov@mail.ru")
                .password("pestrikov123")
                .devices(List.of())
                .build();
    }

    public TaskInfo createTaskInfoByDefault() {
        return TaskMapper.toApi(createTaskByDefault());
    }

    @Test
    void createTask_success() {
        //given
        User user = createUserByDefault();
        Task actualTask = createTaskByDefault();
        TaskInfo expectedTask = createTaskInfoByDefault();
        //when
        when(taskJpaRepository.save(any(Task.class))).thenReturn(actualTask);
        when(userJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        assert user != null;
        TaskInfo afterResponse = taskService.createTask(user.getId(), actualTask);
        //then
        assertNotNull(afterResponse);
        assertEquals(expectedTask,afterResponse);
    }

    @Test
    void createTask_shouldThrowException() {
        //given
        User user = createUserByDefault();
        user.setId(2L);
        Task actualTask = createTaskByDefault();
        //when
        doReturn(Optional.empty()).when(userJpaRepository).findById(anyLong());
        //then
        AuthException thrown = assertThrows(AuthException.class, () -> taskService.createTask(1L, actualTask));
        assertEquals(AuthErrorMessages.USER_NOT_FOUND, thrown.getMessage());
    }

    @Test
    void getTask_success() {
        //given
        Task actualTask = createTaskByDefault();
        TaskInfo expectedTask = createTaskInfoByDefault();
        //when
        when(taskJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(actualTask));
        TaskInfo afterResponse = taskService.getTask(1L, 1L);
        //then
        assertNotNull(afterResponse);
        assertEquals(expectedTask, afterResponse);
    }

    @Test
    void getTask_shouldThrowException() {
        //given

        //when
        doReturn(Optional.empty()).when(taskJpaRepository).findById(anyLong());
        //then
        ClientException thrown = assertThrows(ClientException.class, () -> taskService.getTask(1L,1L));
        assertEquals(ClientErrorMessages.USER_DOESNT_HAVE_CURRENT_TASK, thrown.getMessage());
    }

    @Test
    void updateTask_success() {
        //given
        Task actualTask = createTaskByDefault();
        Task updatedTask = createTaskByDefault();
        updatedTask.setData("new data");
        TaskInfo oldInfo = createTaskInfoByDefault();
        //when
        when(taskJpaRepository.findById(anyLong())).thenReturn(Optional.of(actualTask));
        when(taskJpaRepository.save(any(Task.class))).thenReturn(updatedTask);
        TaskInfo newInfo = taskService.updateTask(1L, actualTask);
        //then
        assertNotEquals(oldInfo,newInfo);
        assertNotNull(newInfo);
    }

    @Test
    void updateTask_shouldThrowException() {
        //given
        Task actualTask = createTaskByDefault();
        //when
        doReturn(Optional.empty()).when(taskJpaRepository).findById(anyLong());
        //then
       ClientException thrown = assertThrows(ClientException.class, () -> taskService.updateTask(1L,actualTask));
       assertEquals(ClientErrorMessages.USER_DOESNT_HAVE_CURRENT_TASK, thrown.getMessage());
    }

    @Test
    void deleteTask_success() {
        //given
        Task actualTask = createTaskByDefault();
        //when
        when(taskJpaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(actualTask));
        taskService.deleteTask(1L, 1L);
        //then
        verify(taskJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_shouldThrowException() {
        //given
        //Task actualTask = createTaskByDefault();
        //when
        when(taskJpaRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        ClientException thrown = assertThrows(ClientException.class, () -> taskService.deleteTask(1L, 1L));
        assertEquals(ClientErrorMessages.USER_DOESNT_HAVE_CURRENT_TASK, thrown.getMessage());
    }

//    @Test
//    void getTasks_shouldReturnCorrectList() {
//        int page = 1;
//        List<Task> tasks = createListOfTasksByDefault();
//        PageRequest pageRequest = PageRequest.of(page,pageSize,Sort.by("created").descending());
//
//        when(taskRepository.findTasksByUserId(1L, pageRequest)).thenReturn(any());
//        assertEquals(tasks, taskService.getTasks(1L,page));
//    }

//    public List<TaskInfo> createListOfTasksInfoByDefault() {
//        return createListOfTasksByDefault()
//                .stream()
//                .map(TaskMapper::toApi)
//                .collect(Collectors.toList());
//    }

//    public List<Task> createListOfTasksByDefault() {
//        return List.of(
//                Task.builder()
//                        .id(1L)
//                        .description("Join in Tinkoff")
//                        .data("Pass an interview")
//                        .user(createUserByDefault())
//                        .build(),
//                Task.builder()
//                        .id(2L)
//                        .description("Breakfast")
//                        .data("Take a meal")
//                        .user(createUserByDefault())
//                        .build()
//        );
//    }
}