package it;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.adapter.rest.task.read.dto.TaskFilterRequestDto;
import com.anst.sd.api.app.api.DateRangeFilter;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadTaskControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/task";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
        project = createProject(user);
    }

    @Test
    void findTaskByFilter_fullFilter() throws Exception {
        createTasksForFilter(project);
        DateRangeFilter dateRangeFilter = new DateRangeFilter();
        dateRangeFilter.setDateFrom(LocalDateTime.now().plusDays(7));
        dateRangeFilter.setDateTo(LocalDateTime.now().plusDays(11));
        TaskFilterRequestDto requestDto = new TaskFilterRequestDto();
        requestDto.setDeadline(dateRangeFilter);
        requestDto.setStatus(List.of(TaskStatus.BACKLOG));
        requestDto.setProjectIds(List.of(project.getId()));

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
            .post(API_URL + "/find-by-filter")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<TaskInfoDto> responseDto = getListFromResponse(response, TaskInfoDto.class);
        assertEquals(1, responseDto.size());
    }

    @Test
    void getTaskById_successfully() throws Exception {
        Task task = createTask(project, null);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/" + task.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TaskInfoDto responseDto = getFromResponse(response, TaskInfoDto.class);
        assertEquals(task.getData(), responseDto.getData());
        assertEquals(task.getId(), responseDto.getId());
        assertEquals(task.getDeadline(), responseDto.getDeadline());
        assertEquals(task.getDescription(), responseDto.getDescription());
        assertEquals(task.getProject().getName(), taskJpaRepository.findAll().get(0).getProject().getName());
        assertEquals(task.getPendingNotifications().size(), responseDto.getPendingNotifications().size());
    }

    @Test
    void getTaskById_withNotification_successfully() throws Exception {
        PendingNotification notification = createNotification();
        Task task = createTask(project, notification);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/" + task.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TaskInfoDto responseDto = getFromResponse(response, TaskInfoDto.class);
        assertEquals(task.getData(), responseDto.getData());
        assertEquals(task.getId(), responseDto.getId());
        assertEquals(task.getPendingNotifications().size(), responseDto.getPendingNotifications().size());
        assertEquals(notification.getTask().getProject().getName(), projectJpaRepository.findAll().get(0).getName());
    }

    @Test
    void getTaskById_failed_notFound() throws Exception {
        performAuthenticated(user, MockMvcRequestBuilders
            .get(API_URL + "/5235"))
            .andDo(print())

            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getTaskList_successfully() throws Exception {
        createTasks(project, 49);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
            .get(API_URL + "/list")
            .param("page", "2")
            .param("projectId", project.getId().toString()))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<TaskInfoDto> responseDto = getListFromResponse(response, TaskInfoDto.class);
        assertEquals(9, responseDto.size());
        assertThat(List.of(41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L),
            containsInAnyOrder(responseDto.stream().map(TaskInfoDto::getId).toArray()));
    }

    @Test
    void getTaskList_successfully_nullPage() throws Exception {
        createTasks(project, 5);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
            .get(API_URL + "/list")
            .param("projectId", project.getId().toString()))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<TaskInfoDto> responseDto = getListFromResponse(response, TaskInfoDto.class);
        assertEquals(5, responseDto.size());
        assertThat(List.of(1L, 2L, 3L, 4L, 5L),
            containsInAnyOrder(responseDto.stream().map(TaskInfoDto::getId).toArray()));
    }

    @Test
    void getTaskList_successfully_emptyPage() throws Exception {
        createTasks(project, 20);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
            .get(API_URL + "/list")
            .param("page", "1")
            .param("projectId", "1"))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<TaskInfoDto> responseDto = getListFromResponse(response, TaskInfoDto.class);
        assertEquals(0, responseDto.size());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<Task> createTasksForFilter(Project project) {
        Task task1 = createBaseTaskForFilter(project);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setDeadline(LocalDateTime.now().plusDays(10));
        task1.setOrderNumber(new BigDecimal("1"));

        Task task2 = createBaseTaskForFilter(project);
        task2.setStatus(TaskStatus.BACKLOG);
        task2.setDeadline(LocalDateTime.now().plusDays(8));
        task2.setOrderNumber(new BigDecimal("2"));

        Task task3 = createBaseTaskForFilter(project);
        task3.setStatus(TaskStatus.DONE);
        task3.setDeadline(LocalDateTime.now().plusDays(6));
        task3.setOrderNumber(new BigDecimal("3"));

        return taskJpaRepository.saveAll(List.of(task1, task2, task3));
    }

    private Task createBaseTaskForFilter(Project project) {
        Task task = new Task();
        task.setData("testData");
        task.setDescription("testData");
        task.setProject(project);
        return task;
    }

    private List<Task> createTasks(Project project, Integer count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            Task task = new Task();
            task.setData("testData" + i);
            task.setStatus(TaskStatus.IN_PROGRESS);
            task.setDescription("testDesc" + i);
            task.setProject(project);
            task.setOrderNumber(new BigDecimal(i+1));
            tasks.add(task);
        }
        return taskJpaRepository.saveAll(tasks);
    }
}
