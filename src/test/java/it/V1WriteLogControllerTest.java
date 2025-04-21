package it;

import com.anst.sd.api.adapter.rest.task.log.dto.CreateUpdateLogDto;
import com.anst.sd.api.domain.task.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
class V1WriteLogControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project/%s/task/%s/logs";
    protected LocalDate now;

    @BeforeEach
    void setUp() {
        user = createTestUser();
        project = createTestProject(user);
        reviewer = createTestReviewer();
        subTask = createSubtask(user, project, reviewer, assignee, null);
        timeEstimation = createTimeEstimation(TimeUnit.HOURS, 3);
        now = LocalDate.now();
    }

    @Test
    void createLog_successfully() throws Exception {
        CreateUpdateLogDto createUpdateLogDto = new CreateUpdateLogDto()
                .setComment("test comment")
                .setTimeEstimation(timeEstimation)
                .setDate(now);

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL.formatted(project.getId(), subTask.getSimpleId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateLogDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(1, logJpaRepository.count());
        Log result = logJpaRepository.findAll().get(0);
        assertEquals(createUpdateLogDto.getComment(), result.getComment());
        assertEquals(createUpdateLogDto.getTimeEstimation().getAmount(), result.getTimeEstimation().getAmount());
        assertEquals(createUpdateLogDto.getTimeEstimation().getTimeUnit(), result.getTimeEstimation().getTimeUnit());
        assertEquals(user.getId(), result.getUser().getId());
        assertNotNull(result.getDate());
        assertEquals(createUpdateLogDto.getDate(), result.getDate());
        assertEquals(subTask.getId(), result.getTask().getId());
    }

    @Test
    void createLog_failed_noUserInProject() throws Exception {
        CreateUpdateLogDto createLogDto = new CreateUpdateLogDto()
                .setComment("test content")
                .setTimeEstimation(timeEstimation)
                .setDate(now);

        performAuthenticated(reviewer, MockMvcRequestBuilders
                .post(API_URL.formatted(project.getId(), subTask.getSimpleId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createLogDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void updateLog_successfully() throws Exception {
        logTask = createLog(subTask, user, "test content", timeEstimation, now);
        CreateUpdateLogDto updateLogDto = new CreateUpdateLogDto()
                .setComment("new content")
                .setTimeEstimation(timeEstimation)
                .setDate(now);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId(), subTask.getSimpleId()) + "/" + logTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLogDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(1, logJpaRepository.count());
        Log result = logJpaRepository.findAll().get(0);
        assertEquals(updateLogDto.getComment(), result.getComment());
        assertEquals(updateLogDto.getTimeEstimation().getTimeUnit(), result.getTimeEstimation().getTimeUnit());
        assertEquals(updateLogDto.getTimeEstimation().getAmount(), result.getTimeEstimation().getAmount());
        assertEquals(updateLogDto.getDate(), result.getDate());
        assertEquals(user.getId(), result.getUser().getId());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());
        assertEquals(subTask.getId(), result.getTask().getId());
    }

    @Test
    void updateLog_failed_userIsAnotherUser() throws Exception {
        logTask = createLog(subTask, reviewer, "test content", timeEstimation, now);
        CreateUpdateLogDto updateLogDto = new CreateUpdateLogDto()
                .setComment("test content")
                .setTimeEstimation(timeEstimation)
                .setDate(now);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId(), subTask.getSimpleId()) + "/" + logTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLogDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteLog_successfully() throws Exception {
        logTask = createLog(subTask, user, "test content", timeEstimation, now);

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL.formatted(project.getId(), subTask.getSimpleId())+ "/" + logTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(0, logJpaRepository.count());
    }

    @Test
    void deleteLog_failed_userIsAnotherUser() throws Exception {
        logTask = createLog(subTask, user, "test content", timeEstimation, now);

        performAuthenticated(reviewer, MockMvcRequestBuilders
                .delete(API_URL.formatted(project.getId(), subTask.getSimpleId())+ "/" + logTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        assertEquals(1, logJpaRepository.count());
    }
}
