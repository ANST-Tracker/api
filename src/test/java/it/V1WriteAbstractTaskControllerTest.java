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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteAbstractTaskControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/task";

    @Test
    void createAbstractTask_successfully() throws Exception {
        user = createTestUser();
        CreateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/createAbstractTask.json",
                CreateAbstractTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractTask task = abstractTaskJpaRepository.findAll().get(0);
        assertEquals(request.getPriority(), task.getPriority());
        assertEquals(request.getDescription(), task.getDescription());
        assertEquals(request.getStoryPoints(), task.getStoryPoints());
        assertEquals(request.getType(), task.getType());
    }

    @Test
    void updateAbstractTask_successfully() throws Exception {
        AbstractTask originalTask = createSubtask();
        UUID taskId = originalTask.getId();
        UpdateAbstractTaskDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTask.json",
                UpdateAbstractTaskDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + taskId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        AbstractTask updateResponse = abstractTaskJpaRepository.findAll().get(0);

        assertNotEquals(originalTask.getName(), updateResponse.getName());
        assertNotEquals(originalTask.getDescription(), updateResponse.getDescription());
        assertNotEquals(originalTask.getPriority(), updateResponse.getPriority());
        assertNotEquals(originalTask.getTimeEstimation(), updateResponse.getTimeEstimation());
    }

    @Test
    void updateStatus_successfully() throws Exception {
        AbstractTask original = createSubtask();
        Subtask originalSubtask = subtaskJpaRepository.findAll().get(0);
        UUID taskId = original.getId();
        UpdateAbstractTaskStatusDto request = readFromFile("/V1WriteAbstractTaskControllerTest/updateAbstractTaskStatus.json",
                UpdateAbstractTaskStatusDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + taskId.toString() + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Subtask updateResponse = subtaskJpaRepository.findAll().get(0);

        assertNotEquals(originalSubtask.getStatus(), updateResponse.getStatus());
    }

}
