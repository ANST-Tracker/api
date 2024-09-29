package it;

import com.anst.sd.api.adapter.rest.tag.dto.TagInfoDto;
import com.anst.sd.api.adapter.rest.tag.write.dto.CreateTagDto;
import com.anst.sd.api.adapter.rest.tag.write.dto.UpdateTagDto;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteTagControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/tag";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
    }

    @Test
    void createTag_successfully() throws Exception {
        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setName("TestTag");
        createTagDto.setColor("#FF0000");

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
        assertEquals(createTagDto.getColor(), responseDto.getColor());
    }

    @Test
    void createTag_tagNameAlreadyExists() throws Exception {
        createTag(user, "OldTag", "#000000");
        CreateTagDto createTagDto = new CreateTagDto();
        createTagDto.setName("OldTag");
        createTagDto.setColor("#FFFFFF");

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTagDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateTag_successfully() throws Exception {
        Tag existingTag = createTag(user, "OldTag", "#000000");

        UpdateTagDto updateTagDto = new UpdateTagDto();
        updateTagDto.setName("UpdatedTag");
        updateTagDto.setColor("#FFFFFF");

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + existingTag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTagDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TagInfoDto responseDto = getFromResponse(response, TagInfoDto.class);
        assertEquals(existingTag.getId(), responseDto.getId());
        assertEquals(updateTagDto.getName(), responseDto.getName());
        assertEquals(updateTagDto.getColor(), responseDto.getColor());
    }

    @Test
    void updateTag_notFound() throws Exception {
        UpdateTagDto updateTagDto = new UpdateTagDto();
        updateTagDto.setName("UpdatedTag");
        updateTagDto.setColor("#FFFFFF");

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTagDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteTag_successfully() throws Exception {
        Tag existingTag = createTag(user, "TagToDelete", "#000000");

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
                .delete(API_URL + "/999999"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Tag createTag(User user, String name, String color) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setColor(color);
        tag.setUser(user);
        return tagJpaRepository.save(tag);
    }
}
