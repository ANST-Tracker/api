package it;

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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadSprintControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/sprint";

    @Test
    void getSprint_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        Sprint sprint = createSprint(project);
        EpicTask epicTask = createEpic(user, project);
        AbstractTask story = createStoryTask(user, project, sprint, epicTask, user, user);
        createDefectTask(user, project, sprint, story);
        createSubtask(user, project, user, user, story);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/" + sprint.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getSprint_failed_notFound() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        Sprint sprint = createSprint(project);
        EpicTask epicTask = createEpic(user, project);
        AbstractTask story = createStoryTask(user, project, sprint, epicTask, user, user);
        createDefectTask(user, project, sprint, story);
        createSubtask(user, project, user, user, story);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
