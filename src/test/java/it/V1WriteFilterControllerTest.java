package it;

import com.anst.sd.api.adapter.rest.filter.dto.CreateFilterDto;
import com.anst.sd.api.adapter.rest.filter.dto.UpdateFilterDto;
import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.domain.filter.FilterPayload;
import com.anst.sd.api.domain.project.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteFilterControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/filter";

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void createFilter_successfully(boolean withProject) throws Exception {
        user = createTestUser();
        Project project = createTestProject(user);
        CreateFilterDto request = readFromFile("/V1WriteFilterControllerTest/abstractDto.json", CreateFilterDto.class);
        if (withProject) {
            request.setProjectId(project.getId());
        }

        createFilter(request);

        Filter result = filterMongoRepository.findAll().get(0);
        assertEquals(user.getId(), result.getUserId());
        assertEquals(request.getPayload(), result.getPayload());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getProjectId(), result.getProjectId());
    }

    @Test
    void createFilter_failed_emptyFilter() throws Exception {
        user = createTestUser();
        CreateFilterDto request = readFromFile("/V1WriteFilterControllerTest/abstractDto.json", CreateFilterDto.class);
        request.setPayload(new FilterPayload());

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateFilter_successfully() throws Exception {
        user = createTestUser();
        CreateFilterDto request = readFromFile("/V1WriteFilterControllerTest/abstractDto.json", CreateFilterDto.class);
        createFilter(request);
        String filterId = filterMongoRepository.findAll().get(0).getId();
        UpdateFilterDto updateFilterDto = readFromFile("/V1WriteFilterControllerTest/abstractDto.json", UpdateFilterDto.class);
        updateFilterDto.setName("newName");
        updateFilterDto.getPayload().setAssigneeIds(null);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/" + filterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateFilterDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Filter result = filterMongoRepository.findAll().get(0);
        assertEquals(user.getId(), result.getUserId());
        assertEquals(updateFilterDto.getPayload(), result.getPayload());
        assertEquals(updateFilterDto.getName(), result.getName());
        assertEquals(request.getProjectId(), result.getProjectId());
    }

    @Test
    void updateFilter_failed_notFound() throws Exception {
        user = createTestUser();
        UpdateFilterDto updateFilterDto = readFromFile("/V1WriteFilterControllerTest/abstractDto.json", UpdateFilterDto.class);

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL + "/notFoundId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateFilterDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteFilter_successfully() throws Exception {
        user = createTestUser();
        CreateFilterDto request = readFromFile("/V1WriteFilterControllerTest/abstractDto.json", CreateFilterDto.class);
        createFilter(request);
        String filterId = filterMongoRepository.findAll().get(0).getId();

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/" + filterId))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(0, filterMongoRepository.count());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void createFilter(CreateFilterDto request) throws Exception {
        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
