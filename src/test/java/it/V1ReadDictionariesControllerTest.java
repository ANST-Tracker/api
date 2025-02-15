package it;

import com.anst.sd.api.domain.task.AbstractTask;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class V1ReadDictionariesControllerTest extends AbstractIntegrationTest {

    private static final String API_URL = "/dictionaries/";

    @Test
    void getAppropriateStatuses_forShortCycleTask_OPEN() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask subtask = createSubtask(user, project);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + subtask.getSimpleId() + "/appropriate-statuses")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code", is("IN_PROGRESS")))
                .andExpect(jsonPath("$[0].value", is("В работе")));
    }

    @Test
    void getAppropriateStatuses_forFullCycleTask_IN_PROGRESS() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        sprint = createSprint(project);
        epicTask = createEpic(user, project);
        AbstractTask storyTask = createStoryTask(user, project, sprint, epicTask);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + storyTask.getSimpleId() + "/appropriate-statuses")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("REVIEW"))
                .andExpect(jsonPath("$[0].value").value("На ревью"))
                .andExpect(jsonPath("$[1].code").value("OPEN"))
                .andExpect(jsonPath("$[1].value").value("Открыта"));
    }
}
