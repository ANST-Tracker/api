package it;

import com.anst.sd.api.AnstApiTodoApplication;
import com.anst.sd.api.adapter.persistence.mongo.UserCodeMongoRepository;
import com.anst.sd.api.adapter.persistence.relational.*;
import com.anst.sd.api.adapter.telegram.CreateUserCodeMessageSupplier;
import com.anst.sd.api.adapter.telegram.TelegramBotFeignClient;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.anst.sd.api.security.domain.ERole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(classes = {AnstApiTodoApplication.class})
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {
    public static final Long DEVICE_ID = 1L;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected DeviceJpaRepository deviceJpaRepository;
    @Autowired
    protected RefreshTokenJpaRepository refreshTokenJpaRepository;
    @Autowired
    protected RoleJpaRepository roleJpaRepository;
    @Autowired
    protected TaskJpaRepository taskJpaRepository;
    @Autowired
    protected UserCodeMongoRepository userCodeMongoRepository;
    @Autowired
    protected ProjectJpaRepository projectJpaRepository;
    @Autowired
    protected UserJpaRepository userJpaRepository;
    @Autowired
    protected NotificationJpaRepository notificationJpaRepository;
    @Autowired
    protected PendingNotificationJpaRepository pendingNotificationJpaRepository;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @MockBean
    protected CreateUserCodeMessageSupplier createUserCodeMessageSupplier;
    @MockBean
    protected TelegramBotFeignClient telegramBotFeignClient;

    protected User user;
    protected Project project;

    @BeforeEach
    void clearDataBase() {
        notificationJpaRepository.deleteAll();
        pendingNotificationJpaRepository.deleteAll();
        userCodeMongoRepository.deleteAll();
        taskJpaRepository.deleteAll();
        projectJpaRepository.deleteAll();
        refreshTokenJpaRepository.deleteAll();
        deviceJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE task_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE project_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE users_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE device_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE refresh_token_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE pending_notification_id_seq RESTART WITH 1");
    }

    protected User createTestUser() {
        User user = new User();
        user.setTelegramId("telegramId");
        user.setPassword("password");
        user.setUsername("username");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setRoles(Set.of(roleJpaRepository.findByName(ERole.USER).get()));
        return userJpaRepository.save(user);
    }

    protected Project createProject(User user) {
        return projectJpaRepository.save(createBaseProject(user, ProjectType.BASE));
    }

    protected Project createProject(User user, ProjectType projectType) {
        return projectJpaRepository.save(createBaseProject(user, ProjectType.BUCKET));
    }

    private Project createBaseProject(User user, ProjectType projectType) {
        Project project = new Project();
        project.setName("test");
        project.setProjectType(projectType);
        project.setUser(user);
        return project;
    }

    protected Task createTask(Project project, PendingNotification pendingNotification) {
        Task task = new Task();
        task.setData("testData");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDescription("testData");
        task.setProject(project);
        if (pendingNotification != null) {
            pendingNotification.setTask(task);
            task.setPendingNotifications(List.of(pendingNotification));
        }
        return taskJpaRepository.save(task);
    }

    protected PendingNotification createNotification() {
        PendingNotification pendingNotification = new PendingNotification();
        pendingNotification.setAmount(10);
        pendingNotification.setTimeType(TimeUnit.DAYS);
        pendingNotification.setExecutionDate(Instant.now().plusSeconds(1000));
        return pendingNotification;
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

    protected <T> T readFromFile(String fileName) {
        String content = readFile(fileName);
        try {
            return objectMapper.readValue(content, new TypeReference<>() {
            });
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
        JwtResponse result = jwtService.generateAccessRefreshTokens(user.getUsername(), user.getId(), DEVICE_ID, ERole.USER);
        return result.getAccessToken();
    }
}