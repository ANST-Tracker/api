package it;

import com.anst.sd.api.adapter.rest.project.write.dto.CreateProjectDto;
import com.anst.sd.api.adapter.rest.project.write.dto.UpdateProjectDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteProjectControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project";

    @Test
    void updateProject_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        UpdateProjectDto request = readFromFile("/V1WriteProjectControllerTest/updateProjectDto.json", UpdateProjectDto.class);
        request.setHeadId(project.getHead().getId());

        performAuthenticated(user, MockMvcRequestBuilders
            .put(API_URL + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        project = projectJpaRepository.findAll().get(0);
        assertEquals(request.getName(), project.getName());
        assertEquals(request.getDescription(), project.getDescription());
        assertEquals(request.getHeadId(), project.getHead().getId());
    }

    @Test
    void updateProject_failed_validationError() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        UpdateProjectDto request = readFromFile("/V1WriteProjectControllerTest/updateProjectDto.json", UpdateProjectDto.class);
        request.setHeadId(project.getHead().getId());
        request.setName("");

        performAuthenticated(user, MockMvcRequestBuilders
            .put(API_URL + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateProject_failed_notFound() throws Exception {
        User user = createTestUser();
        UpdateProjectDto request = readFromFile("/V1WriteProjectControllerTest/updateProjectDto.json", UpdateProjectDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
            .put(API_URL + "/e4d909c2-90d0-fb1c-a068-ffaddf22cbd0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createProject_failed_userNotFound() throws Exception {
        User user = createTestUser();
        CreateProjectDto request = readFromFile("/V1WriteProjectControllerTest/createProjectDto.json", CreateProjectDto.class);
        userJpaRepository.deleteAll();

        performAuthenticated(user, MockMvcRequestBuilders
            .post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createProject_successfully() throws Exception {
        User user = createTestUser();
        CreateProjectDto request = readFromFile("/V1WriteProjectControllerTest/createProjectDto.json", CreateProjectDto.class);
        request.setHeadId(user.getId());
        performAuthenticated(user, MockMvcRequestBuilders
            .post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        Project project = projectJpaRepository.findAll().get(0);
        assertEquals(request.getName(), project.getName());
        assertEquals(request.getDescription(), project.getDescription());
        assertEquals(request.getHeadId(), project.getHead().getId());
        assertEquals(request.getKey(), project.getKey());
    }

    @Test
    void createProject_failed_validationError() throws Exception {
        User user = createTestUser();
        CreateProjectDto request = readFromFile("/V1WriteProjectControllerTest/createProjectDto.json", CreateProjectDto.class);
        request.setName("");

        performAuthenticated(user, MockMvcRequestBuilders
            .post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
