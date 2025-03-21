package it;

import com.anst.sd.api.adapter.rest.usersProjects.write.dto.AddUserInProjectDto;
import com.anst.sd.api.domain.PermissionCode;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class V1WriteUsersProjectsControllerTest extends AbstractIntegrationTest{
    private static final String API_URL = "/users-projects";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
        project = createTestProject(user);
    }

    @Test
    void addUserInProject_successfully() throws Exception {
        AddUserInProjectDto addUserInProjectDto = new AddUserInProjectDto();
        addUserInProjectDto.setProjectId(project.getId());
        addUserInProjectDto.setUserId(user.getId());
        addUserInProjectDto.setPermissionCode("READ_ONLY");

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserInProjectDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UsersProjects usersProjects = usersProjectsJpaRepository.findAll().get(0);
        assertNotNull(usersProjects.getId());
        assertEquals(addUserInProjectDto.getUserId(), usersProjects.getUser().getId());
        assertEquals(addUserInProjectDto.getProjectId(), usersProjects.getProject().getId());
        assertEquals(PermissionCode.READ_ONLY, usersProjects.getPermissionCode());
    }

    @Test
    void addUserInProject_connectionAlreadyExists() throws Exception {
        createUsersProjects(project, user);
        AddUserInProjectDto addUserInProjectDto = new AddUserInProjectDto();
        addUserInProjectDto.setProjectId(project.getId());
        addUserInProjectDto.setUserId(user.getId());
        addUserInProjectDto.setPermissionCode("READ_ONLY");

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserInProjectDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void removeUserFromProject_successfully() throws Exception {
        UsersProjects existingUsersProjects = createUsersProjects(project, user);
        String removeUserFromProjectUrl = String.format("/projects/%s/users/%s",
                existingUsersProjects.getProject().getId(),
                existingUsersProjects.getUser().getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + removeUserFromProjectUrl))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertFalse(usersProjectsJpaRepository.existsById(existingUsersProjects.getId()));
    }

    @Test
    void removeUserFromProject_notFound() throws Exception {
        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/project/07dd8345-d82f-4cde-badb-c74bd73876a2/user/07dd8345-d82f-4cde-aadb-c74bd73876a2/user"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private UsersProjects createUsersProjects(Project project, User user) {
        UsersProjects usersProjects = new UsersProjects();
        usersProjects.setUser(user);
        usersProjects.setProject(project);
        usersProjects.setPermissionCode(PermissionCode.READ_ONLY);
        return usersProjectsJpaRepository.save(usersProjects);
    }

}
