package it;

import com.anst.sd.api.adapter.rest.sprint.read.dto.CreateSprintDto;
import com.anst.sd.api.adapter.rest.sprint.read.dto.UpdateSprintDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.EpicTask;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteSprintControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project/%s/sprint";

    @Test
    void createSprint_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        CreateSprintDto request = readFromFile("/V1WriteSprintControllerTest/createSprint.json", CreateSprintDto.class);
        request.setProjectId(project.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL.formatted(project.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
        Sprint sprint = sprintJpaRepository.findAll().get(0);
        assertEquals(request.getName(), sprint.getName());
        assertEquals(request.getDescription(), sprint.getDescription());
        assertEquals(request.getIsActive(), sprint.getIsActive());
        assertEquals(request.getStartDate(), sprint.getStartDate());
        assertEquals(request.getEndDate(), sprint.getEndDate());
    }

    @Test
    void updateSprint_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        Sprint sprint = createSprint(project);
        UpdateSprintDto request = readFromFile("/V1WriteSprintControllerTest/updateSprint.json", UpdateSprintDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId()) + "/" + sprint.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
        sprint = sprintJpaRepository.findAll().get(0);
        assertEquals(request.getName(), sprint.getName());
        assertEquals(request.getDescription(), sprint.getDescription());
        assertEquals(request.getIsActive(), sprint.getIsActive());
        assertEquals(request.getStartDate(), sprint.getStartDate());
        assertEquals(request.getEndDate(), sprint.getEndDate());
    }

    @Test
    void updateSprint_failed_notFound() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        createSprint(project);
        UpdateSprintDto request = readFromFile("/V1WriteSprintControllerTest/updateSprint.json", UpdateSprintDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId()) + "/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void attachTaskToSprint_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        Sprint sprint = createSprint(project);
        EpicTask epicTask = createEpic(user, project);
        AbstractTask storyTask = createStoryTask(user, project, sprint, epicTask, user, user);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId()) + "/" + sprint.getId() + "/task/" + storyTask.getSimpleId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void attachTaskToSprint_failed_sprintNotFound() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        Sprint sprint = createSprint(project);
        EpicTask epicTask = createEpic(user, project);
        AbstractTask storyTask = createStoryTask(user, project, sprint, epicTask, user, user);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId()) + "/" + UUID.randomUUID() + "/task/" + storyTask.getSimpleId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void attachTaskToSprint_failed_taskNotFound() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        Sprint sprint = createSprint(project);
        EpicTask epicTask = createEpic(user, project);
        createStoryTask(user, project, sprint, epicTask, user, user);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId()) + "/" + sprint.getId() + "/task/RANDOM")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
