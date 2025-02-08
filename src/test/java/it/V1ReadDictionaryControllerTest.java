package it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadDictionaryControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/dictionary/statuses";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
    }

    @Test
    void getShortCycleDictionary_successfully() throws Exception {
        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/short")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.OPEN", containsInAnyOrder("IN_PROGRESS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_PROGRESS", containsInAnyOrder("REVIEW", "CLOSED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.REVIEW", containsInAnyOrder("CLOSED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.CLOSED").isEmpty());
    }

    @Test
    void getFullCycleDictionary_successfully() throws Exception {
        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/full")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.OPEN", containsInAnyOrder("IN_PROGRESS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_PROGRESS", containsInAnyOrder("REVIEW", "RESOLVED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.REVIEW", containsInAnyOrder("RESOLVED", "QA_READY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.RESOLVED", containsInAnyOrder("QA_READY", "CLOSED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.QA_READY", containsInAnyOrder("IN_QA", "CLOSED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_QA", containsInAnyOrder("CLOSED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.CLOSED").isEmpty());
    }
}