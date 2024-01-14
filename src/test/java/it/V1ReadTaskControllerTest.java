package it;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.adapter.rest.task.read.dto.TaskFilterRequestDto;
import com.anst.sd.api.app.api.DateRangeFilter;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadTaskControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/task";
    private User user;

    @BeforeEach
    void prepareData() {
        user = createTestUser();
    }

    @Test
    void findTaskByFilter_fullFilter() throws Exception {
        createTasksForFilter(user);
        DateRangeFilter dateRangeFilter = new DateRangeFilter();
        dateRangeFilter.setDateFrom(LocalDateTime.now().plusDays(7));
        dateRangeFilter.setDateTo(LocalDateTime.now().plusDays(11));
        TaskFilterRequestDto requestDto = new TaskFilterRequestDto();
        requestDto.setDeadline(dateRangeFilter);
        requestDto.setStatus(List.of(TaskStatus.BACKLOG));

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
        Task task = createTask(user);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/" + task.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TaskInfoDto responseDto = getFromResponse(response, TaskInfoDto.class);
        assertEquals("testData", responseDto.getData());
        assertEquals(task.getId(), responseDto.getId());
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
        createTasks(user, 49);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/list")
                .param("page", "2"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TaskInfoDto> responseDto = getListFromResponse(response, TaskInfoDto.class);
        assertEquals(9, responseDto.size());
        assertThat(List.of(41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L),
                containsInAnyOrder(responseDto.stream().map(TaskInfoDto::getId).toArray()));
    }

    @Test
    void getTaskList_successfully_emptyPage() throws Exception {
        createTasks(user, 20);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/list")
                .param("page", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TaskInfoDto> responseDto = getListFromResponse(response, TaskInfoDto.class);
        assertEquals(0, responseDto.size());
    }

    private List<Task> createTasksForFilter(User user) {
        Task task1 = createBaseTaskForFilter(user);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setDeadline(LocalDateTime.now().plusDays(10));

        Task task2 = createBaseTaskForFilter(user);
        task2.setStatus(TaskStatus.BACKLOG);
        task2.setDeadline(LocalDateTime.now().plusDays(8));

        Task task3 = createBaseTaskForFilter(user);
        task3.setStatus(TaskStatus.DONE);
        task3.setDeadline(LocalDateTime.now().plusDays(6));

        return taskJpaRepository.saveAll(List.of(task1, task2, task3));
    }

    private Task createBaseTaskForFilter(User user) {
        Task task = new Task();
        task.setData("testData");
        task.setDescription("testData");
        task.setUser(user);
        return task;
    }

    private Task createTask(User user) {
        Task task = new Task();
        task.setData("testData");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDescription("testData");
        task.setUser(user);
        return taskJpaRepository.save(task);
    }

    private List<Task> createTasks(User user, Integer count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            Task task = new Task();
            task.setData("testData" + i);
            task.setStatus(TaskStatus.IN_PROGRESS);
            task.setDescription("testDesc" + i);
            task.setUser(user);

            tasks.add(task);
        }
        return taskJpaRepository.saveAll(tasks);
    }
}
