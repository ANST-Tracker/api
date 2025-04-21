package it;

import com.anst.sd.api.adapter.rest.sprint.dto.SprintInfoDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class V1ReadSprintControllerTest extends AbstractIntegrationTest{
    private static final String API_URL = "/project/%s/sprint";

    @Test
    void getSprints_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        createSprint(project);
        createSprint(project);
        User user1 = createUser("Enemy", "@youEnemy", "kak@mail.com");
        Project project1 = createTestProject(user1);
        createSprint(project1);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL.formatted(project.getId()) + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<SprintInfoDto> sprintInfoDtoList = getListFromResponse(response, SprintInfoDto.class);
        assertEquals(2, sprintInfoDtoList.size());
        sprintInfoDtoList.forEach(sprintInfoDto -> {
            assertNotNull(sprintInfoDto.getId());
            assertNotNull(sprintInfoDto.getStartDate());
            assertNotNull(sprintInfoDto.getEndDate());
            sprintInfoDto.setId(null);
            sprintInfoDto.setStartDate(null);
            sprintInfoDto.setEndDate(null);
        });
        assertEqualsToFile("/V1ReadSprintControllerTest/expectedSprint.json", sprintInfoDtoList);
    }

    @Test
    void getSprints_userNotInProject_Unauthorized() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        createSprint(project);
        createSprint(project);
        User user1 = createUser("Enemy", "@youEnemy", "kak@mail.com");
        Project project1 = createTestProject(user1);
        createSprint(project1);

        performAuthenticated(user1, MockMvcRequestBuilders
                .get(API_URL.formatted(project.getId()) + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
