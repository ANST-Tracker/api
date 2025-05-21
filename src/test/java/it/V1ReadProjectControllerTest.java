package it;

import com.anst.sd.api.adapter.rest.project.read.dto.ProjectInfoDto;
import com.anst.sd.api.adapter.rest.tag.dto.TagInfoDto;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadProjectControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project";

    @Test
    void getProject_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        List<Tag> tags = new ArrayList<>();
        List<User> users = new ArrayList<>();
        int countUsersTags = 5;
        for (int i = 1; i < countUsersTags; i++) {
            User newUser = createUser(
                    "username" + i,
                    "email" + i,
                    "telegramId" + i,
                    null, null);
            createUsersProjects(project, newUser);
            users.add(newUser);
            tags.add(createTag(project, "tag" + i));
        }

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/" + project.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        project = projectJpaRepository.findAll().get(0);
        ProjectInfoDto responseDto = getFromResponse(response, ProjectInfoDto.class);
        assertEquals(responseDto.getName(), project.getName());
        assertEquals(responseDto.getDescription(), project.getDescription());
        assertEquals(responseDto.getHead().getId(), project.getHead().getId());
        for (int i = 0; i < countUsersTags - 1; i++) {
            User userTest = users.get(i);
            UserInfoDto userRes = responseDto.getUsers().get(i);

            assertEquals(userTest.getFirstName(), userRes.getFirstName());
            assertEquals(userTest.getLastName(), userRes.getLastName());
            assertEquals(userTest.getPosition(), userRes.getPosition());
            assertEquals(userTest.getDepartmentName(), userRes.getDepartmentName());
            assertEquals(userTest.getTelegramId(), userRes.getTelegramId());
            assertEquals(userTest.getId(), userRes.getId());

            Tag tagTest = tags.get(i);
            TagInfoDto tagRes = responseDto.getTags().get(i);
            assertEquals(tagTest.getName(), tagRes.getName());
            assertEquals(tagTest.getProject().getId(), tagRes.getProjectId());
        }
    }

    @Test
    void getProject_NotFound() throws Exception {
        User user = createTestUser();
        createTestProject(user);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/e4d909c2-90d0-fb1c-a068-ffaddf22cbd0"))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getProjects_successfully() throws Exception {
        User user = createTestUser();
        int countProjects = 3;
        for (int i = 0; i < countProjects; i++) {
            createTestProject(user);
        }

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/all"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Project> projects = projectJpaRepository.findAllByUserId(user.getId());
        List<ProjectInfoDto> responseDto = getListFromResponse(response, ProjectInfoDto.class);
        assertEquals(projects.size(), responseDto.size());
        for (int i = 0; i < countProjects; i++) {
            assertEquals(responseDto.get(i).getName(), projects.get(i).getName());
            assertEquals(responseDto.get(i).getDescription(), projects.get(i).getDescription());
            assertEquals(responseDto.get(i).getHead().getId(), projects.get(i).getHead().getId());
        }
    }
}
