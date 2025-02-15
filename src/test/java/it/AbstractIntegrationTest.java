package it;

import com.anst.sd.api.AnstApiTodoApplication;
import com.anst.sd.api.adapter.persistence.relational.AbstractTaskJpaRepository;
import com.anst.sd.api.adapter.persistence.relational.SubtaskJpaRepository;
import com.anst.sd.api.adapter.persistence.relational.ProjectJpaRepository;
import com.anst.sd.api.adapter.persistence.relational.UserJpaRepository;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.*;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.Position;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(classes = {AnstApiTodoApplication.class})
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {
    protected static final UUID DEVICE_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected RedissonClient redissonClient;
    @Autowired
    protected UserJpaRepository userJpaRepository;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected ProjectJpaRepository projectJpaRepository;

    protected User user;
    protected Project project;
    protected AbstractTaskJpaRepository abstractTaskJpaRepository;
    @Autowired
    protected SubtaskJpaRepository subtaskJpaRepository;
    protected User user;
    protected Sprint sprint;

    @BeforeEach
    void clearDataBase() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        abstractTaskJpaRepository.deleteAll();
        projectJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    protected User createTestUser() {
        User user = new User();
        user.setUsername("username");
        user.setId(UUID.randomUUID());
        user.setPassword("testPassword");
        user.setTelegramId("eridiium");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("test@com");
        user.setPosition(Position.DEVOPS);
        user.setDepartmentName("HSE");
        user.setRegistrationDate(LocalDate.now());
        user.setTimeZone(1);
        user.setCreated(Instant.now());
        return userJpaRepository.save(user);
    }

    protected Project createTestProject(User headUser) {
        Project project = new Project();
        project.setName("Project1");
        project.setDescription("New test project");
        project.setHead(headUser);
        project.setNextTaskId(1);
        project.setKey("P1");
        return projectJpaRepository.save(project);
    }

    protected AbstractTask createSubtask() {
        Subtask task = new Subtask();
        user = createTestUser();
        task.setName("Test Subtask");
        task.setDescription("This is a test subtask");
        task.setSimpleId("GD-1");
        task.setType(TaskType.SUBTASK);
        task.setStatus(ShortCycleStatus.OPEN);
        task.setPriority(TaskPriority.MAJOR);
        task.setStoryPoints(5);
        task.setAssignee(user);
        task.setReviewer(user);
        task.setCreator(user);
        task.setProject(null);
        task.setDueDate(LocalDate.now().plusDays(7));
        task.setOrderNumber(BigDecimal.ONE);
        task.setTimeEstimation(null);
        task.setTags(List.of());
        return abstractTaskJpaRepository.save(task);
    }

    protected AbstractTask createStoryTask() {
        StoryTask task = new StoryTask();
        user = createTestUser();
        task.setName("Test StoryTask");
        task.setDescription("This is a test storytask");
        task.setSimpleId("GD-2");
        task.setType(TaskType.STORY);
        task.setStatus(FullCycleStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.MAJOR);
        task.setStoryPoints(5);
        task.setAssignee(user);
        task.setReviewer(user);
        task.setSprint(null);
        task.setEpicTask(null);
        task.setTester(user);
        task.setCreator(user);
        task.setProject(null);
        task.setDueDate(LocalDate.now().plusDays(7));
        task.setOrderNumber(BigDecimal.ONE);
        task.setTimeEstimation(null);
        task.setTags(List.of());
        return abstractTaskJpaRepository.save(task);
    }

    // ===================================================================================================================
    // = ObjectMapper utils
    // ===================================================================================================================

    protected <T> T mapToObject(String string, Class<?> clazz, Class<?>... classes) {
        try {
            if (classes.length == 0) {
                return (T) objectMapper.readValue(string, clazz);
            }

            Class<?>[] newClasses = ArrayUtils.addFirst(classes, clazz);
            JavaType currentType = null;
            for (int i = newClasses.length - 1; i > 0; i--) {
                if (currentType == null) {
                    currentType =
                            objectMapper.getTypeFactory().constructParametricType(newClasses[i - 1], newClasses[i]);
                } else {
                    currentType = objectMapper.getTypeFactory().constructParametricType(newClasses[i - 1], currentType);
                }
            }

            return objectMapper.readValue(string, currentType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===================================================================================================================
    // = File utils
    // ===================================================================================================================

    protected String readFile(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            return Files.readString(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected <T> T readFromFile(String fileName, Class<T> clazz) {
        String content = readFile(fileName);
        try {
            return objectMapper.readValue(content, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T readFromFile(String fileName, Class<?> clazz, Class<?>... classes) {
        String content = readFile(fileName);
        return mapToObject(content, clazz, classes);
    }

    protected <T> List<T> readListFromFile(String fileName, Class<?>... classes) {
        return readFromFile(fileName, List.class, classes);
    }

    protected void assertEqualsToFile(String fileName, Object value) {
        String expected = readFile(fileName);
        try {
            String actual = objectMapper.writeValueAsString(value);
            Assertions.assertEquals(expected, actual);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // ===================================================================================================================
    // = Request/Response utils
    // ===================================================================================================================

    protected ResultActions performAuthenticated(User user, MockHttpServletRequestBuilder content) throws Exception {
        return mockMvc.perform(content
                .header("Authorization", "Bearer " + createAuthData(user))
        );
    }

    protected String getStringFromResponse(MvcResult result) {
        try {
            return result.getResponse().getContentAsString(UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T getFromResponse(MvcResult result, Class<?> clazz, Class<?>... classes) {
        return mapToObject(getStringFromResponse(result), clazz, classes);
    }

    protected <T> List<T> getListFromResponse(MvcResult result, Class<?>... classes) {
        try {
            return getFromResponse(result, List.class, classes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String createAuthData(User user) {
        JwtResponse result = jwtService.generateAccessRefreshTokens(user.getUsername(), user.getId(), DEVICE_UUID);
        return result.getAccessToken();
    }
}