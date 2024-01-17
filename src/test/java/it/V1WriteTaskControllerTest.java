package it;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.anst.sd.api.domain.task.TaskStatus.BACKLOG;
import static com.anst.sd.api.domain.task.TaskStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteTaskControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/task";
    private User user;
    private Project project;

    @BeforeEach
    void prepareData() {
        user = createTestUser();
        project = createProject(user);
    }

    @Test
    void createTask_failed_userNotFound() throws Exception {
        CreateTaskDto request = readFromFile("/V1WriteTaskControllerTest/createTaskDto.json", CreateTaskDto.class);
        userJpaRepository.deleteAll();

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

        MvcResult result = performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TaskInfoDto dto = getFromResponse(result, TaskInfoDto.class);
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getData(), dto.getData());
        assertEquals(request.getDeadline(), dto.getDeadline());
        assertEquals(BACKLOG, dto.getStatus());
        Task task = taskJpaRepository.findAll().get(0);
        assertNotNull(task.getCreated());
    }

    @Test
    void createTask_failed_validationError() throws Exception {
        CreateTaskDto request = readFromFile("/V1WriteTaskControllerTest/createTaskDto.json", CreateTaskDto.class);
        request.setData("");

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteTask_successfully() throws Exception {
        Task task = createTask(project);

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/" + task.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(0, taskJpaRepository.findAll().size());
    }

    @Test
    void deleteTask_failed_notFound() throws Exception {
        createTask(project);

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/5433"))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateTask_successfully() throws Exception {
        Task task = createTask(project);
        UpdateTaskDto request = readFromFile("/V1WriteTaskControllerTest/updateTaskDto.json", UpdateTaskDto.class);

        MvcResult result = performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TaskInfoDto dto = getFromResponse(result, TaskInfoDto.class);
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getData(), dto.getData());
        assertEquals(request.getDeadline(), dto.getDeadline());
        assertEquals(IN_PROGRESS, dto.getStatus());
        task = taskJpaRepository.findAll().get(0);
        assertNotNull(task.getUpdated());
    }

    @Test
    void updateTask_failed_validationError() throws Exception {
        Task task = createTask(project);
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

    private Task createTask(Project project) {
        Task task = new Task();
        task.setData("testData");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDescription("testData");
        task.setProject(project);
        return taskJpaRepository.save(task);
    }

    private Project createProject(User user) {
        Project project = new Project();
        project.setName("test");
        project.setProjectType(ProjectType.BASE);
        project.setUser(user);
        return projectJpaRepository.save(project);
    }
}
