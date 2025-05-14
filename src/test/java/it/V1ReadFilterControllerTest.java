package it;

import com.anst.sd.api.adapter.rest.filter.dto.CreateFilterDto;
import com.anst.sd.api.adapter.rest.filter.dto.FilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadFilterControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/filter";

    @Test
    void getFilters_successfully() throws Exception {
        user = createTestUser();
        createFilter(null);
        createFilter(null);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/all"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<FilterDto> actualFilters = getListFromResponse(response, FilterDto.class);
        actualFilters.forEach(filter -> {
            assertNotNull(filter.getId());
            filter.setId(null);
        });
        assertEqualsToFile("/V1ReadFilterControllerTest/expectedAll.json", actualFilters);
    }

    @Test
    void getFilters_successfully_emptyList() throws Exception {
        user = createTestUser();

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/all"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<FilterDto> actualFilters = getListFromResponse(response, FilterDto.class);
        assertEquals(0, actualFilters.size());
    }

    @Test
    void getFilters_byProject_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        createFilter(project.getId());
        createFilter(project.getId());

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/by-project/" + project.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<FilterDto> actualFilters = getListFromResponse(response, FilterDto.class);
        actualFilters.forEach(filter -> {
            assertNotNull(filter.getId());
            filter.setId(null);
            assertEquals(project.getId(), filter.getProjectId());
            filter.setProjectId(null);
        });
        assertEqualsToFile("/V1ReadFilterControllerTest/expectedAll.json", actualFilters);
    }

    @Test
    void getFilters_byProject_successfully_emptyList() throws Exception {
        user = createTestUser();
        project = createTestProject(user);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/by-project/" + project.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<FilterDto> actualFilters = getListFromResponse(response, FilterDto.class);
        assertEquals(0, actualFilters.size());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void createFilter(UUID projectId) throws Exception {
        CreateFilterDto request = readFromFile("/V1ReadFilterControllerTest/createFilterDto.json", CreateFilterDto.class);
        request.setProjectId(projectId);
        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
