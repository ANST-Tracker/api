package it;

import com.anst.sd.api.AnstApiTodoApplication;
import com.anst.sd.api.adapter.persistence.mongo.FilterMongoRepository;
import com.anst.sd.api.adapter.persistence.mongo.UserCodeMongoRepository;
import com.anst.sd.api.adapter.persistence.relational.*;
import com.anst.sd.api.adapter.telegram.CreateUserCodeMessageSupplier;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.*;
import com.anst.sd.api.adapter.persistence.relational.DeviceJpaRepository;
import com.anst.sd.api.adapter.persistence.relational.ProjectJpaRepository;
import com.anst.sd.api.adapter.persistence.relational.UserJpaRepository;
import com.anst.sd.api.adapter.telegram.CreateUserCodeMessageSupplier;
import com.anst.sd.api.domain.PermissionCode;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.Position;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    protected static final UUID DEVICE_ID = UUID.randomUUID();
    protected static final String USER_PASSWORD = UUID.randomUUID().toString();

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserJpaRepository userJpaRepository;
    @Autowired
    protected DeviceJpaRepository deviceJpaRepository;
    @Autowired
    protected UserCodeMongoRepository userCodeMongoRepository;
    @MockBean
    protected CreateUserCodeMessageSupplier createUserCodeMessageSupplier;
    @Autowired
    protected ProjectJpaRepository projectJpaRepository;
    @Autowired
    protected AbstractTaskJpaRepository abstractTaskJpaRepository;
    @Autowired
    protected SubtaskJpaRepository subtaskJpaRepository;
    @Autowired
    protected EpicTaskJpaRepository epicTaskJpaRepository;
    @Autowired
    protected SprintJpaRepository sprintJpaRepository;
    @Autowired
    protected StoryTaskJpaRepository storyTaskJpaRepository;
    protected User user;
    protected User reviewer;
    protected User assignee;
    protected Project project;
    protected EpicTask epicTask;
    protected Sprint sprint;
    protected AbstractTask storyTask;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected FilterMongoRepository filterMongoRepository;

    @BeforeEach
    void clearDataBase() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        subtaskJpaRepository.deleteAll();
        storyTaskJpaRepository.deleteAll();
        epicTaskJpaRepository.deleteAll();
        sprintJpaRepository.deleteAll();
        abstractTaskJpaRepository.deleteAll();
        filterMongoRepository.deleteAll();
        projectJpaRepository.deleteAll();
        userCodeMongoRepository.deleteAll();
        deviceJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    protected User createTestUser() {
        return createUser("username", "eridiium", "eridiium@gmail.com");
    }

    protected User createTestReviewer() {
        return createUser("reviewer", "reviewer", "reviewer@gmail.com");
    }

    protected User createTestAssignee() {
        return createUser("assignee", "assignee", "assignee@gmail.com");
    }

    private User createUser(String username, String telegramId, String email) {
        User user = new User();
        user.setUsername(username);
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        user.setTelegramId(telegramId);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
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
        project.setKey("GD");
        return projectJpaRepository.save(project);
    }

    protected AbstractTask createSubtask(User user, Project project, User reviewer, User assignee, AbstractTask storyTask) {
        Subtask task = new Subtask();
        task.setName("Test Subtask");
        task.setDescription("This is a test subtask");
        task.setSimpleId("GD-2");
        task.setType(TaskType.SUBTASK);
        task.setStatus(TaskStatus.OPEN);
        task.setReviewer(reviewer);
        task.setAssignee(assignee);
        task.setStoryTask((StoryTask) storyTask);
        fillAbstractTaskFields(task, user, project);
        return abstractTaskJpaRepository.save(task);
    }

    protected AbstractTask createStoryTask(User user, Project project, Sprint sprint, EpicTask epicTask,
                                           User reviewer, User assignee) {
        StoryTask task = new StoryTask();
        task.setName("Test StoryTask");
        task.setDescription("This is a test storytask");
        task.setSimpleId("GD-2");
        task.setType(TaskType.STORY);
        task.setStatus(TaskStatus.IN_PROGRESS);
        fillAbstractTaskFields(task, user, project);
        task.setSprint(sprint);
        task.setEpicTask(epicTask);
        task.setReviewer(reviewer);
        task.setAssignee(assignee);
        task.setTester(user);
        return abstractTaskJpaRepository.save(task);
    }

    protected Sprint createSprint(Project project) {
        Sprint sprint = new Sprint();
        sprint.setProject(project);
        sprint.setCreated(Instant.now());
        sprint.setName("sprint");
        sprint.setStartDate(LocalDate.now().minusDays(1));
        sprint.setEndDate(LocalDate.now().plusDays(14));
        sprint.setIsActive(true);
        return sprintJpaRepository.save(sprint);
    }

    protected EpicTask createEpic(User user, Project project) {
        EpicTask epicTask = new EpicTask();
        epicTask.setSimpleId("GD-3");
        epicTask.setName("Epic");
        epicTask.setType(TaskType.EPIC);
        epicTask.setStatus(TaskStatus.OPEN);
        epicTask.setDescription("description");
        fillAbstractTaskFields(epicTask, user, project);
        return epicTaskJpaRepository.save(epicTask);
    }

    protected <T extends AbstractTask> void fillAbstractTaskFields(T task, User user, Project project) {
        task.setPriority(TaskPriority.MAJOR);
        task.setStoryPoints(5);
        task.setAssignee(user);
        task.setReviewer(user);
        task.setCreator(user);
        task.setProject(project);
        task.setDueDate(LocalDate.now().plusDays(7));
        task.setOrderNumber(BigDecimal.ONE);
        task.setTimeEstimation(null);
        task.setTags(List.of());
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
        JwtResponse result = jwtService.generateAccessRefreshTokens(user.getUsername(), user.getId(), DEVICE_ID);
        return result.getAccessToken();
    }

    protected User createTestUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setTelegramId("telegramId");
        user.setDepartmentName("departmentName");
        user.setEmail("email");
        user.setPosition(Position.PM);
        user.setRegistrationDate(LocalDate.now());
        user.setTimeZone(5);
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
        project.setUsers(List.of(
            new UsersProjects()
                .setProject(project)
                .setUser(headUser)
                .setPermissionCode(PermissionCode.READ_WRITE)
        ));
        return projectJpaRepository.save(project);
    }
}