package it;

import com.anst.sd.api.adapter.rest.project.write.dto.CreateProjectDto;
import com.anst.sd.api.adapter.rest.project.write.dto.UpdateProjectDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.anst.sd.api.domain.project.ProjectType.BASE;
import static com.anst.sd.api.domain.project.ProjectType.BUCKET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteProjectControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
    }

    @Test
    void deleteProject_successfully() throws Exception {
        Project project = createProject(user, BASE);

        performAuthenticated(user, MockMvcRequestBuilders
            .delete(API_URL + "/" + project.getId()))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        assertEquals(0, taskJpaRepository.findAll().size());
    }

    @Test
    void deleteProject_failed_notFound() throws Exception {
        performAuthenticated(user, MockMvcRequestBuilders
            .delete(API_URL + "/5433"))
            .andDo(print())

            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteProject_bucket() throws Exception {
        Project project = createProject(user, BUCKET);

        performAuthenticated(user, MockMvcRequestBuilders
            .delete(API_URL + "/" + project.getId()))
            .andDo(print())

            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateProject_successfully() throws Exception {
        Project project = createProject(user, BASE);
        UpdateProjectDto request = readFromFile("/V1WriteProjectControllerTest/updateProjectDto.json", UpdateProjectDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
            .put(API_URL + "/" + project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        project = projectJpaRepository.findAll().get(0);
        assertEquals(request.getName(), project.getName());
        assertNotNull(project.getUpdated());
    }

    @Test
    void updateProject_failed_validationError() throws Exception {
        Project project = createProject(user, BASE);
        UpdateProjectDto request = readFromFile("/V1WriteProjectControllerTest/updateProjectDto.json", UpdateProjectDto.class);
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
        UpdateProjectDto request = readFromFile("/V1WriteProjectControllerTest/updateProjectDto.json", UpdateProjectDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
            .put(API_URL + "/5433")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())

            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createProject_failed_userNotFound() throws Exception {
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
        CreateProjectDto request = readFromFile("/V1WriteProjectControllerTest/createProjectDto.json", CreateProjectDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
            .post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        Project project = projectJpaRepository.findAll().get(0);
        assertEquals(request.getName(), project.getName());
        assertEquals(BASE, project.getProjectType());
        assertEquals(user.getId(), project.getUser().getId());
        assertNotNull(project.getCreated());
    }

    @Test
    void createProject_failed_validationError() throws Exception {
        CreateProjectDto request = readFromFile("/V1WriteProjectControllerTest/createProjectDto.json", CreateProjectDto.class);
        request.setName("");

        performAuthenticated(user, MockMvcRequestBuilders
            .post(API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())

            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    private Project createProject(User user, ProjectType projectType) {
//        Project project = new Project();
//        project.setUser(user);
//        project.setName("test");
//        project.setProjectType(projectType);
//        return projectJpaRepository.save(project);
//    }
}
