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

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;
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
    @Autowired
    protected LogJpaRepository logJpaRepository;
    protected User user;
    protected User reviewer;
    protected User assignee;
    protected Project project;
    protected EpicTask epicTask;
    protected Sprint sprint;
    protected AbstractTask storyTask;
    protected AbstractTask subTask;
    protected TimeEstimation timeEstimation;
    protected Log logTask;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected FilterMongoRepository filterMongoRepository;

    @BeforeEach
    void clearDataBase() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        logJpaRepository.deleteAll();
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
        return createUser("username", "eridiium", "eridiium@gmail.com", null, null);
    }

    protected User createTestReviewer() {
        return createUser("reviewer", "reviewer", "reviewer@gmail.com", null, null);
    }

    protected User createTestAssignee() {
        return createUser("assignee", "assignee", "assignee@gmail.com", null, null);
    }

    protected User createUserWithName(String firstName, String lastName) {
        String userData = firstName + " " + lastName;
        return createUser(userData, userData, userData, firstName, lastName);
    }

    protected User createUser(String username, String telegramId, String email, String firstName, String lastName) {
        User userTest = new User();
        userTest.setUsername(username);
        userTest.setId(UUID.randomUUID());
        userTest.setPassword(passwordEncoder.encode(USER_PASSWORD));
        userTest.setTelegramId(telegramId);
        userTest.setFirstName(firstName == null ? "firstName" : firstName);
        userTest.setLastName(lastName == null ? "lastName" : lastName);
        userTest.setEmail(email);
        userTest.setPosition(Position.DEVOPS);
        userTest.setDepartmentName("HSE");
        userTest.setRegistrationDate(LocalDate.now());
        userTest.setTimeZone(1);
        userTest.setCreated(Instant.now());
        return userJpaRepository.save(userTest);
    }

    protected Project createTestProject(User headUser) {
        Project projectTest = new Project();
        projectTest.setName("Project1");
        projectTest.setDescription("New test project");
        projectTest.setHead(headUser);
        projectTest.setNextTaskId(1);
        projectTest.setKey("GD");
        return projectJpaRepository.save(projectTest);
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
        task.setSimpleId("GD-4");
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

    protected AbstractTask createDefectTask(User user, Project project, Sprint sprint, AbstractTask storyTask) {
        DefectTask defectTask = new DefectTask();
        defectTask.setName("Test defect");
        defectTask.setDescription("This is a test defect");
        defectTask.setSprint(sprint);
        defectTask.setType(TaskType.DEFECT);
        defectTask.setStatus(TaskStatus.IN_PROGRESS);
        defectTask.setStoryTask((StoryTask) storyTask);
        fillAbstractTaskFields(defectTask, user, project);
        defectTask.setSimpleId("GD-3");
        defectTask.setTester(user);
        defectTask.setSprint(sprint);
        defectTask.setPriority(TaskPriority.MAJOR);
        return abstractTaskJpaRepository.save(defectTask);
    }

    protected Sprint createSprint(Project project) {
        Sprint task = new Sprint();
        task.setProject(project);
        task.setCreated(Instant.now());
        task.setName("sprint");
        task.setDescription("description");
        task.setStartDate(LocalDate.of(2020, 1, 1));
        task.setEndDate(LocalDate.of(2020, 12, 1));
        task.setIsActive(true);
        return sprintJpaRepository.save(task);
    }

    protected EpicTask createEpic(User user, Project project) {
        EpicTask task = new EpicTask();
        task.setSimpleId("GD-3");
        task.setName("Epic");
        task.setType(TaskType.EPIC);
        task.setStatus(TaskStatus.OPEN);
        task.setDescription("description");
        task.setTimeEstimation(new TimeEstimation()
                .setTimeUnit(TimeUnit.HOURS)
                .setAmount(10));
        fillAbstractTaskFields(task, user, project);
        return epicTaskJpaRepository.save(task);
    }

    protected EpicTask createEpic(User user, Project project, List<Tag> tags) {
        EpicTask task = new EpicTask();
        task.setSimpleId("GD-3");
        task.setName("Epic");
        task.setType(TaskType.EPIC);
        task.setStatus(TaskStatus.OPEN);
        task.setDescription("description");
        task.setTags(tags);
        fillAbstractTaskFields(task, user, project);
        return epicTaskJpaRepository.save(task);
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
                .setTemplate(NotificationTemplate.NEW_TASK_IN_PROJECT)
                .setCreationDateTime(Instant.parse("2025-04-22T12:00:00Z"))
                .setParams(Map.of(
                        USER_NAME.getKey(), "testUser",
                        TASK_TITLE.getKey(), "Реализовать тестовую задачу",
                        TASK_TYPE.getKey(), TaskType.EPIC.getValue(),
                        PROJECT_NAME.getKey(), "project"
                ));
        notification.setTemplate(NotificationTemplate.NEW_TASK_IN_PROJECT);
        return notificationMongoRepository.save(notification);
    }

    protected Log createLog(AbstractTask task, User user, String comment, TimeEstimation estimation, LocalDate date) {
        Log log = new Log()
                .setUser(user)
                .setTask(task)
                .setComment(comment)
                .setTimeEstimation(estimation)
                .setDate(date);
        return logJpaRepository.save(log);
    }

    protected TimeEstimation createTimeEstimation(TimeUnit timeUnit, Integer amount) {
        TimeEstimation timeEstimationTest = new TimeEstimation();
        return timeEstimationTest.setTimeUnit(timeUnit).setAmount(amount);
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