package it;

import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadNotificationControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/notifications";

    @Test
    void getNotifications_successfully() throws Exception {
        User user = createTestUser();
        createNotification(user);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getNotifications_emptyList_successfully() throws Exception {
        User user = createTestUser();

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
