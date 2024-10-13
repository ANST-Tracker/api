package it;

import com.anst.sd.api.adapter.rest.task.dto.PendingNotificationDto;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskOrderNumberDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.anst.sd.api.domain.task.TaskStatus.BACKLOG;
import static com.anst.sd.api.domain.task.TaskStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteTaskControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/task";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
        project = createProject(user);
    }

    @Test
    void createTask_failed_userNotFound() throws Exception {
        userJpaRepository.deleteAll();
        CreateTaskDto request = readFromFile("/V1WriteTaskControllerTest/createTaskDto.json", CreateTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createTask_successfully() throws Exception {
        CreateTaskDto request = readFromFile("/V1WriteTaskControllerTest/createTaskDto.json", CreateTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL + "/" + project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Task task = taskJpaRepository.findAll().get(0);
        assertEquals(request.getDescription(), task.getDescription());
        assertEquals(request.getData(), task.getData());
        assertEquals(request.getDeadline(), task.getDeadline());
        assertEquals(BACKLOG, task.getStatus());
        assertNotNull(task.getCreated());
    }

    @Test
    void createTask_failed_validationError() throws Exception {
        CreateTaskDto request = readFromFile("/V1WriteTaskControllerTest/createTaskDto.json", CreateTaskDto.class);
        request.setData("");

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL + "/" + project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createTask_withNotification_successfully() throws Exception {
        CreateTaskDto request = readFromFile("/V1WriteTaskControllerTest/createTaskDto.json", CreateTaskDto.class);
        PendingNotificationDto notification = createPendingNotificationDto();
        request.setPendingNotifications(List.of(notification));

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL + "/" + project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(1, pendingNotificationJpaRepository.findAll().size());
    }

    @Test
    void deleteTask_withNotification_successfully() throws Exception {
        Task task = createTask(project, createNotification());

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/" + task.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(0, taskJpaRepository.findAll().size());
        assertEquals(0, pendingNotificationJpaRepository.findAll().size());
    }

    @Test
    void deleteTask_failed_notFound() throws Exception {
        createTask(project, createNotification());

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/5433"))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateTask_successfully() throws Exception {
        Task task = createTask(project, createNotification());
        UpdateTaskDto request = readFromFile("/V1WriteTaskControllerTest/updateTaskDto.json", UpdateTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        task = taskJpaRepository.findAll().get(0);
        assertEquals(request.getDescription(), task.getDescription());
        assertEquals(request.getData(), task.getData());
        assertEquals(request.getDeadline(), task.getDeadline());
        assertEquals(IN_PROGRESS, task.getStatus());
        assertNotNull(task.getUpdated());
    }

    @Test
    void updateTask_moveToProject() throws Exception {
        Task task = createTask(project, createNotification());
        Project anotherProject = createProject(user);
        UpdateTaskDto request = readFromFile("/V1WriteTaskControllerTest/updateTaskDto.json", UpdateTaskDto.class);
        request.setUpdatedProjectId(anotherProject.getId());

        performAuthenticated(user, MockMvcRequestBuilders
            .put(API_URL + "/" + task.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        task = taskJpaRepository.findAll().get(0);
        assertEquals(request.getDescription(), task.getDescription());
        assertEquals(anotherProject.getId(), task.getProject().getId());
        assertEquals(request.getData(), task.getData());
        assertEquals(request.getDeadline(), task.getDeadline());
        assertEquals(IN_PROGRESS, task.getStatus());
        assertNotNull(task.getUpdated());
    }

    @Test
    void updateTask_failed_validationError() throws Exception {
        Task task = createTask(project, null);
        UpdateTaskDto request = readFromFile("/V1WriteTaskControllerTest/updateTaskDto.json", UpdateTaskDto.class);
        request.setData("");

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateTask_failed_notFound() throws Exception {
        UpdateTaskDto request = readFromFile("/V1WriteTaskControllerTest/updateTaskDto.json", UpdateTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/5433")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateTask_withNotification_successfully() throws Exception {
        Task task = createTask(project, createNotification());
        UpdateTaskDto request = readFromFile("/V1WriteTaskControllerTest/updateTaskDto.json", UpdateTaskDto.class);
        PendingNotificationDto notification = createPendingNotificationDto();
        request.setPendingNotifications(List.of(notification));

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(1, pendingNotificationJpaRepository.findAll().size());
    }

    @Test
    void updateTaskOrderNumber_successfully() throws Exception {
        Task task = createTask(project, createNotification());
        UpdateTaskOrderNumberDto request = readFromFile("/V1WriteTaskControllerTest/updateTaskOrderNumberDto.json", UpdateTaskOrderNumberDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + task.getId() + "/orderNumber")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        task = taskJpaRepository.findAll().get(0);
        assertEquals(request.getOrderNumber(), task.getOrderNumber());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private PendingNotificationDto createPendingNotificationDto() {
        PendingNotificationDto pendingNotification = new PendingNotificationDto();
        pendingNotification.setAmount(10);
        pendingNotification.setTimeType(TimeUnit.DAYS);
        return pendingNotification;
    }
}
