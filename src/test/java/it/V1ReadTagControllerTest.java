package it;

import com.anst.sd.api.adapter.rest.tag.dto.TagInfoDto;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadTagControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/tag";

    private User user;

    @BeforeEach
    void prepareData() {
        user = createTestUser();
    }

    @Test
    void getTags_successfully() throws Exception {
        createTags(user, 5);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TagInfoDto> responseDto = getListFromResponse(response, TagInfoDto.class);
        assertEquals(5, responseDto.size());
    }

    @Test
    void getTags_emptyList() throws Exception {
        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TagInfoDto> responseDto = getListFromResponse(response, TagInfoDto.class);
        assertTrue(responseDto.isEmpty());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<Tag> createTags(User user, int count) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Tag tag = new Tag();
            tag.setName("Tag" + i);
            tag.setColor("#" + String.format("%06x", i * 100000));
            tag.setUser(user);
            tags.add(tag);
        }
        return tagJpaRepository.saveAll(tags);
    }
}
