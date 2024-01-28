package it;

import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadProjectControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project";
    private User user;

    @BeforeEach
    void prepareData() {
        user = createTestUser();
    }

    @Test
    void getProjectList_successfully() throws Exception {
        List<Project> savedProjects = createProjects(user, 10);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
            .get(API_URL + "/list"))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<ProjectInfoDto> responseDto = getListFromResponse(response, ProjectInfoDto.class);
        assertEquals(savedProjects.size(), responseDto.size());
        List<String> expectedNames = savedProjects.stream().map(Project::getName).toList();
        List<String> actualNames = responseDto.stream().map(ProjectInfoDto::getName).toList();
        assertThat(actualNames, containsInAnyOrder(expectedNames.toArray()));
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<Project> createProjects(User user, int amount) {
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Project project = new Project();
            project.setProjectType(ProjectType.BASE);
            project.setUser(user);
            project.setName("test" + i);
            projects.add(project);
        }
        return projectJpaRepository.saveAll(projects);
    }
}
