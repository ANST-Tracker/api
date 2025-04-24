package it;

import com.anst.sd.api.AnstApiTodoApplication;
import com.anst.sd.api.adapter.persistence.mongo.FilterMongoRepository;
import com.anst.sd.api.adapter.persistence.mongo.NotificationMongoRepository;
import com.anst.sd.api.adapter.persistence.mongo.UserCodeMongoRepository;
import com.anst.sd.api.adapter.persistence.relational.*;
import com.anst.sd.api.adapter.telegram.CreateUserCodeMessageSupplier;
import com.anst.sd.api.domain.PermissionCode;
import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.notification.Notification;
import com.anst.sd.api.domain.notification.NotificationTemplate;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.*;
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
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    protected TagJpaRepository tagJpaRepository;
    @Autowired
    protected UsersProjectsJpaRepository usersProjectsJpaRepository;
    @Autowired
    protected DefectTaskJpaRepository defectTaskJpaRepository;
    @Autowired
    protected CommentJpaRepository commentJpaRepository;
    @Autowired
    protected NotificationMongoRepository notificationMongoRepository;
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
        commentJpaRepository.deleteAll();
        subtaskJpaRepository.deleteAll();
        defectTaskJpaRepository.deleteAll();
        storyTaskJpaRepository.deleteAll();
        epicTaskJpaRepository.deleteAll();
        sprintJpaRepository.deleteAll();
        abstractTaskJpaRepository.deleteAll();
        tagJpaRepository.deleteAll();
        usersProjectsJpaRepository.deleteAll();
        filterMongoRepository.deleteAll();
        tagJpaRepository.deleteAll();
        projectJpaRepository.deleteAll();
        userCodeMongoRepository.deleteAll();
        notificationMongoRepository.deleteAll();
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

    protected User createUser(String username, String telegramId, String email) {
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
        project.setUsers(List.of(
                new UsersProjects()
                        .setPermissionCode(PermissionCode.READ_WRITE)
                        .setProject(project)
                        .setUser(headUser)
        ));
        return projectJpaRepository.save(project);
    }

    protected UsersProjects createUsersProjects(Project project, User user) {
        UsersProjects usersProjects = new UsersProjects();
        usersProjects.setUser(user);
        usersProjects.setProject(project);
        usersProjects.setPermissionCode(PermissionCode.READ_ONLY);
        return usersProjectsJpaRepository.save(usersProjects);
    }

    protected Tag createTag(Project project, String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setProject(project);
        return tagJpaRepository.save(tag);
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
        epicTask.setTimeEstimation(new TimeEstimation()
                .setTimeUnit(TimeUnit.HOURS)
                .setAmount(10));
        fillAbstractTaskFields(epicTask, user, project);
        return epicTaskJpaRepository.save(epicTask);
    }

    protected EpicTask createEpic(User user, Project project, List<Tag> tags) {
        EpicTask epicTask = new EpicTask();
        epicTask.setSimpleId("GD-3");
        epicTask.setName("Epic");
        epicTask.setType(TaskType.EPIC);
        epicTask.setStatus(TaskStatus.OPEN);
        epicTask.setDescription("description");
        epicTask.setTags(tags);
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
        task.setDueDate(LocalDate.of(2025, 4, 12));
        task.setOrderNumber(BigDecimal.ONE);
        task.setTags(List.of(
                createTag("tag1", project),
                createTag("tag2", project)
        ));
    }

    private Tag createTag(String name, Project project) {
        Tag tag = new Tag()
                .setName(name)
                .setProject(project);
        return tagJpaRepository.save(tag);
    }

    protected Comment createComment(AbstractTask task, User user, String content) {
        Comment comment = new Comment()
                .setAuthor(user)
                .setContent(content)
                .setTask(task);
        return commentJpaRepository.save(comment);
    }

    protected Notification createNotification(User user) {
        Notification notification = new Notification()
                .setViewed(false)
                .setRecipientLogin(user.getUsername())
                .setRecipientTelegramId(user.getTelegramId())
                .setTemplate(NotificationTemplate.TASK_NEW_ASSIGNEE)
                .setCreationDateTime(Instant.parse("2025-04-22T12:00:00Z"))
                .setParams(Map.of(
                        "userLogin", "testUser",
                        "taskTitle", "afasf",
                        "link", "www.google.com"
                ));
        return notificationMongoRepository.save(notification);
    }

    // ===================================================================================================================
    // = ObjectMapper utils
    // ===================================================================================================================

    protected void nullifyAllIdFields(Object obj) {
        nullifyAllIdFields(obj, new IdentityHashMap<>());
    }

    private void nullifyAllIdFields(Object obj, Map<Object, Boolean> visited) {
        if (obj == null || visited.containsKey(obj)) return;
        visited.put(obj, true);

        Class<?> clazz = obj.getClass();

        if (clazz.isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(obj, i);
                nullifyAllIdFields(element, visited);
            }
            return;
        }

        if (obj instanceof Collection<?>) {
            for (Object item : (Collection<?>) obj) {
                nullifyAllIdFields(item, visited);
            }
            return;
        }

        if (obj instanceof Map<?, ?>) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                nullifyAllIdFields(entry.getKey(), visited);
                nullifyAllIdFields(entry.getValue(), visited);
            }
            return;
        }

        if (isJavaBuiltin(clazz)) return;

        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    if ("id".equals(field.getName())) {
                        if (!field.getType().isPrimitive()) {
                            field.set(obj, null);
                        }
                    } else {
                        nullifyAllIdFields(value, visited);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field: " + field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private boolean isJavaBuiltin(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz.getName().startsWith("java.")
                || clazz.getName().startsWith("javax.")
                || clazz == String.class
                || Number.class.isAssignableFrom(clazz)
                || clazz == Boolean.class
                || clazz.isEnum();
    }

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
}