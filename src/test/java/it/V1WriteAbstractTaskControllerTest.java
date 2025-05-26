package it;

import com.anst.sd.api.adapter.rest.task.write.dto.CreateAbstractTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskStatusDto;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.Subtask;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.anst.sd.api.domain.task.TaskType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteAbstractTaskControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/task";

    @Test
    void createAbstractTask_successfully() throws Exception {
        CreateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/createAbstractTask.json",
                CreateAbstractTaskDto.class);
        user = createTestUser();
        reviewer = createTestReviewer();
        assignee = createTestAssignee();
        project = createTestProject(user);
        sprint = createSprint(project);
        epicTask = createEpic(user, project);
        storyTask = createStoryTask(user, project, sprint, epicTask, reviewer, assignee);
        createSubtask(user, project, reviewer, assignee, storyTask);
        request.setProjectId(project.getId());
        request.setReviewerId(reviewer.getId());
        request.setAssigneeId(assignee.getId());
        request.setStoryTaskId(storyTask.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractTask result = abstractTaskJpaRepository.findAll().stream()
                .filter(task -> "Under Test".equals(task.getName()))
                .findFirst().orElseThrow();
        assertEquals(request.getPriority(), result.getPriority());
        assertEquals(request.getStoryPoints(), result.getStoryPoints());
        assertEquals(request.getType(), result.getType());
        assertNotNull(result.getProject());
        assertNotNull(result.getCreator());
        assertNotNull(result.getAssignee());
    }

    @Test
    void createStoryTask_successfully() throws Exception {
        CreateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/createStoryTask.json",
                CreateAbstractTaskDto.class);
        user = createTestUser();
        reviewer = createTestReviewer();
        assignee = createTestAssignee();
        project = createTestProject(user);
        sprint = createSprint(project);
        epicTask = createEpic(user, project);
        storyTask = createStoryTask(user, project, sprint, epicTask, reviewer, assignee);
        request.setProjectId(project.getId());
        request.setReviewerId(reviewer.getId());
        request.setAssigneeId(assignee.getId());
        request.setEpicTaskId(epicTask.getId());
        request.setSprintId(sprint.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractTask result = abstractTaskJpaRepository.findAll().stream()
                .filter(task -> "Under Test".equals(task.getName()))
                .findFirst().orElseThrow();
        assertEquals(request.getPriority(), result.getPriority());
        assertEquals(request.getStoryPoints(), result.getStoryPoints());
        assertEquals(request.getType(), result.getType());
        assertNotNull(result.getProject());
        assertNotNull(result.getCreator());
        assertNotNull(result.getAssignee());
    }

    @Test
    void createAbstractTask_invalidDto_returnsBadRequest() throws Exception {
        CreateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/createAbstractTask.json",
                CreateAbstractTaskDto.class);
        request.setName(null);
        user = createTestUser();

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateAbstractTask_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        sprint = createSprint(project);
        epicTask = createEpic(user, project);
        storyTask = createStoryTask(user, project, sprint, epicTask, reviewer, assignee);
        AbstractTask originalTask = createSubtask(user, project, null, null, storyTask);
        String simpleId = originalTask.getSimpleId();
        UpdateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTask.json",
                UpdateAbstractTaskDto.class);
        request.setStoryTaskId(storyTask.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + simpleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractTask updateResult = abstractTaskJpaRepository.findAll().stream()
                .filter(task -> SUBTASK.equals(task.getType()))
                .findFirst().orElseThrow();
        assertNotEquals(originalTask.getName(), updateResult.getName());
        assertNotEquals(originalTask.getDescription(), updateResult.getDescription());
        assertNotEquals(originalTask.getPriority(), updateResult.getPriority());
        assertNotEquals(originalTask.getTimeEstimation(), updateResult.getTimeEstimation());
    }

    @Test
    void updateEpic_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask originalTask = createEpic(user, project);
        String simpleId = originalTask.getSimpleId();
        UpdateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateEpicTask.json",
                UpdateAbstractTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + simpleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractTask updateResult = abstractTaskJpaRepository.findAll().stream()
                .filter(task -> EPIC.equals(task.getType()))
                .findFirst().orElseThrow();
        assertNotEquals(originalTask.getName(), updateResult.getName());
        assertNotEquals(originalTask.getDescription(), updateResult.getDescription());
        assertNotEquals(originalTask.getPriority(), updateResult.getPriority());
        assertNotEquals(originalTask.getTimeEstimation(), updateResult.getTimeEstimation());
    }

    @Test
    void updateStory_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        sprint = createSprint(project);
        epicTask = createEpic(user, project);
        AbstractTask originalTask = createStoryTask(user, project, sprint, epicTask, null, null);
        String simpleId = originalTask.getSimpleId();
        UpdateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateStoryTask.json",
                UpdateAbstractTaskDto.class);
        request.setEpicTaskId(epicTask.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + simpleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractTask updateResult = abstractTaskJpaRepository.findAll().stream()
                .filter(task -> STORY.equals(task.getType()))
                .findFirst().orElseThrow();
        assertNotEquals(originalTask.getName(), updateResult.getName());
        assertNotEquals(originalTask.getDescription(), updateResult.getDescription());
        assertNotEquals(originalTask.getPriority(), updateResult.getPriority());
    }

    @Test
    void updateAbstractTask_nonExistentSimpleId_returnsNotFound() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        createSubtask(user, project, null, null, null);
        String nonExistentSimpleId = "NON_EXISTENT_ID";
        UpdateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTask.json",
                UpdateAbstractTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + nonExistentSimpleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateAbstractTask_invalidDto_returnsBadRequest() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask originalTask = createSubtask(user, project, reviewer, assignee, null);
        String simpleId = originalTask.getSimpleId();
        UpdateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTask.json",
                UpdateAbstractTaskDto.class);
        request.setName(null);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + simpleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStatus_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask original = createSubtask(user, project, null, null, null);
        Subtask originalSubtask = subtaskJpaRepository.findAll().get(0);
        String simpleId = original.getSimpleId();
        UpdateAbstractTaskStatusDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTaskStatus.json",
                UpdateAbstractTaskStatusDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + simpleId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Subtask updateResponse = subtaskJpaRepository.findAll().get(0);

        assertNotEquals(originalSubtask.getStatus(), updateResponse.getStatus());
    }

    @Test
    void updateStatus_nonExistentSimpleId_returnsNotFound() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        createSubtask(user, project, null, null, null);

        String nonExistentSimpleId = "NON_EXISTENT_ID";
        UpdateAbstractTaskStatusDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTaskStatus.json",
                UpdateAbstractTaskStatusDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + nonExistentSimpleId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateStatus_invalidStatusValue_returnsBadRequest() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask original = createSubtask(user, project, null, null, null);
        String simpleId = original.getSimpleId();
        UpdateAbstractTaskStatusDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTaskStatus.json",
                UpdateAbstractTaskStatusDto.class);
        request.setStatus(null);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + simpleId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
