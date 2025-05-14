package it;

import com.anst.sd.api.adapter.rest.task.dto.EpicTaskInfoDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskFilterDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskRegistryDto;
import com.anst.sd.api.domain.filter.FilterPayload;
import com.anst.sd.api.domain.task.TaskPriority;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.task.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Comparator;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadTaskControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/task";

    @Test
    void findByFilter_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        epicTask = createEpic(user, project);
        sprint = createSprint(project);
        storyTask = createStoryTask(user, project, sprint, epicTask, user, user);
        createSubtask(user, project, user, user, storyTask);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL + "/find-by-filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createFilter())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TaskRegistryDto> actualTasks = getListFromResponse(response, TaskRegistryDto.class);
        actualTasks.sort(Comparator.comparing(TaskRegistryDto::getSimpleId));
        assertEqualsToFile("/V1ReadTaskControllerTest/expectedTasks.json", actualTasks);
    }

    @Test
    void getTask_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        epicTask = createEpic(user, project);
        sprint = createSprint(project);
        storyTask = createStoryTask(user, project, sprint, epicTask, user, user);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/" + epicTask.getSimpleId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        EpicTaskInfoDto task = getFromResponse(response, EpicTaskInfoDto.class);
        nullifyAllIdFields(task);
        assertEqualsToFile("/V1ReadTaskControllerTest/expectedTask.json", task);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private TaskFilterDto createFilter() {
        return new TaskFilterDto()
                .setProjectId(project.getId())
                .setPayload(new FilterPayload()
                        .setCreatorIds(List.of(user.getId()))
                        .setAssigneeIds(List.of(user.getId()))
                        .setStatuses(List.of(TaskStatus.OPEN))
                        .setPriorities(List.of(TaskPriority.MAJOR))
                        .setTypes(List.of(TaskType.EPIC, TaskType.STORY, TaskType.SUBTASK)));
    }
}
