package it;

import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.anst.sd.api.domain.task.TaskStatus.BACKLOG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteTaskInternalControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/internal/task";

    @BeforeEach
    void setUp() {
        user = createTestUser();
    }

    @Test
    void createTask_successfully() throws Exception {
        String taskName = "taskName";
        createProject(user, ProjectType.BUCKET);

        mockMvc.perform(MockMvcRequestBuilders
            .post(API_URL + "/" + user.getTelegramId() + "/" + taskName))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        Task task = taskJpaRepository.findAll().get(0);
        assertEquals(taskName, task.getData());
        assertEquals(BACKLOG, task.getStatus());
        assertNotNull(task.getCreated());
    }

    @Test
    void createTask_failed_noBucket() throws Exception {
        String taskName = "taskName";

        mockMvc.perform(MockMvcRequestBuilders
                .post(API_URL + "/" + user.getTelegramId() + "/" + taskName))
            .andDo(print())

            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
