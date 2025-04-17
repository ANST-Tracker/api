package it;

import com.anst.sd.api.adapter.rest.task.log.dto.CreateUpdateLogDto;
import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
class V1WriteLogControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project/%s/task/%s/logs";

    @Test
    void createLog_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        TimeEstimation timeEstimation = createTimeEstimation(TimeUnit.HOURS, 3);
        CreateUpdateLogDto createUpdateLogDto = new CreateUpdateLogDto()
                .setComment("test comment")
                .setTimeEstimation(timeEstimation);


        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL.formatted(project.getId(), subtask.getSimpleId()))
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
        assertNotNull(result.getCreated());
        assertEquals(subtask.getId(), result.getTask().getId());
    }

    @Test
    void createLog_failed_noUserInProject() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        reviewer = createTestReviewer();
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        TimeEstimation timeEstimation = createTimeEstimation(TimeUnit.HOURS, 3);
        CreateUpdateLogDto createLogDto = new CreateUpdateLogDto()
                .setComment("test content")
                .setTimeEstimation(timeEstimation);

        performAuthenticated(reviewer, MockMvcRequestBuilders
                .post(API_URL.formatted(project.getId(), subtask.getSimpleId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createLogDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void updateLog_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        TimeEstimation timeEstimation = createTimeEstimation(TimeUnit.HOURS, 3);
        Log log = createLog(subtask, user, "test content", timeEstimation);
        CreateUpdateLogDto updateLogDto = new CreateUpdateLogDto()
                .setComment("new content")
                .setTimeEstimation(timeEstimation);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId(), subtask.getSimpleId()) + "/" + log.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLogDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(1, logJpaRepository.count());
        Log result = logJpaRepository.findAll().get(0);
        assertEquals(updateLogDto.getComment(), result.getComment());
        assertEquals(updateLogDto.getTimeEstimation().getTimeUnit(), result.getTimeEstimation().getTimeUnit());
        assertEquals(updateLogDto.getTimeEstimation().getAmount(), result.getTimeEstimation().getAmount());
        assertEquals(user.getId(), result.getUser().getId());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());
        assertEquals(subtask.getId(), result.getTask().getId());
    }

    @Test
    void updateLog_failed_userIsAnotherUser() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        reviewer = createTestReviewer();
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        TimeEstimation timeEstimation = createTimeEstimation(TimeUnit.HOURS, 3);
        Log log = createLog(subtask, reviewer, "test content", timeEstimation);
        CreateUpdateLogDto updateLogDto = new CreateUpdateLogDto()
                .setComment("test content")
                .setTimeEstimation(timeEstimation);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId(), subtask.getSimpleId()) + "/" + log.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLogDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
