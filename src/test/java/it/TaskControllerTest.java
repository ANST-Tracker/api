package it;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class TaskControllerTest extends AbstractIntegrationTest {

    @Test
    void getTaskById_successfully() throws Exception {
        User user = createTestUser();
        Task task = createTask(user);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get("/task/" + task.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TaskInfoDto responseDto = getFromResponse(response, TaskInfoDto.class);
        assertEquals("testData", responseDto.getData());
        assertEquals(task.getId(), responseDto.getId());
    }

    private Task createTask(User user) {
        Task task = new Task();
        task.setData("testData");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDescription("testData");
        task.setUser(user);
        return taskJpaRepository.save(task);
    }
}
