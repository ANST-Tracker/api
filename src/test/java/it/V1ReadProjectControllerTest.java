package it;

import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDto;
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

public class V1ReadProjectControllerTest extends AbstractIntegrationTest{
    private static final String API_URL = "/project";

    @Test
    void getProject_successfully() throws Exception {
        User user = createTestUser();
        Project project = createTestProject(user);
        List<Tag> tags = new ArrayList<>();
        List<User> users = new ArrayList<>();
        users.add(user);
        int countUsersTags = 5;
        for(int i = 1;i < countUsersTags;i++){
            User newUser = createUser(
                    "username" + String.valueOf(i),
                    "email" + String.valueOf(i),
                    "telegramId" + String.valueOf(i));
            createUsersProjects(project, newUser);
            users.add(newUser);
            tags.add(createTag(project,"tag" + String.valueOf(i)));
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
        assertEquals(responseDto.getHeadId(), project.getHead().getId());
        for(int i = 0;i < countUsersTags-1;i++){
            User userTest = users.get(i);
            UserInfoDto userRes = responseDto.getUsers().get(i);

            assertEquals(userTest.getFirstName(), userRes.getFirstName());
            assertEquals(userTest.getLastName(), userRes.getLastName());
            assertEquals(userTest.getPosition(), userRes.getPosition());
            assertEquals(userTest.getPassword(), userRes.getPassword());
            assertEquals(userTest.getDepartmentName(), userRes.getDepartmentName());
            assertEquals(userTest.getTelegramId(), userRes.getTelegramId());

            Tag tagTest = tags.get(i);
            TagInfoDto tagRes = responseDto.getTags().get(i);
            assertEquals(tagTest.getName(), tagRes.getName());
            assertEquals(tagTest.getProject().getId(), tagRes.getProjectId());
        }

    }

    @Test
    void getProject_NotFound() throws Exception{
        User user = createTestUser();
        Project project = createTestProject(user);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/e4d909c2-90d0-fb1c-a068-ffaddf22cbd0"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
