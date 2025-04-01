package it;

import com.anst.sd.api.adapter.rest.tag.dto.TagInfoDto;
import com.anst.sd.api.adapter.rest.tag.write.dto.CreateTagDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.AbstractTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteTagControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/tag";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
        project = createTestProject(user);
    }

    @Test
    void createTag_successfully() throws Exception {
        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setName("TestTag");
        createTagDto.setProjectId(project.getId());
        createUsersProjects(project, user);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTagDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TagInfoDto responseDto = getFromResponse(response, TagInfoDto.class);
        assertNotNull(responseDto.getId());
        assertEquals(createTagDto.getName(), responseDto.getName());
        assertEquals(createTagDto.getProjectId(),responseDto.getProjectId());
    }

    @Test
    void createTag_tagNameAlreadyExists() throws Exception {
        createTag(project, "OldTag");
        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setName("OldTag");
        createTagDto.setProjectId(project.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTagDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createTag_withSameNameInDifferentProjects_successfully() throws Exception {
        createUsersProjects(project, user);
        createTag(project, "TestTag");
        Project newProject = createTestProject(user);
        createUsersProjects(newProject, user);

        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setName("TestTag");
        createTagDto.setProjectId(newProject.getId());

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTagDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TagInfoDto responseDto = getFromResponse(response, TagInfoDto.class);
        assertNotNull(responseDto.getId());
        assertEquals(createTagDto.getName(), responseDto.getName());
        assertEquals(createTagDto.getProjectId(),responseDto.getProjectId());
    }

    @Test
    void deleteTag_successfully() throws Exception {
        Tag existingTag = createTag(project, "TagToDelete");
        createUsersProjects(project, user);

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/" + existingTag.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(tagJpaRepository.existsById(existingTag.getId()));
    }

    @Test
    void deleteTagWithTask_successfully() throws Exception {
        Tag existingTag = createTag(project, "TagToDelete");
        List<Tag> tags = new ArrayList<>();
        tags.add(existingTag);
        AbstractTask task = createEpic(user, project, tags);
        createUsersProjects(project, user);

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/" + existingTag.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(tagJpaRepository.existsById(existingTag.getId()));
    }

    @Test
    void deleteTag_notFound() throws Exception {
        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/07dd8345-d82f-4cde-badb-c74bd73876a2"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
