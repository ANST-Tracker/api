package it;

import com.anst.sd.api.adapter.rest.notification.dto.NotificationInfoDto;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class V1ReadNotificationControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/notifications";

    @Test
    void getNotifications_successfully() throws Exception {
        User user = createTestUser();
        createNotification(user);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders.get(API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<NotificationInfoDto> notifications = getListFromResponse(response, NotificationInfoDto.class);
        NotificationInfoDto dto = notifications.get(0);
        assertEqualsToFile("/V1ReadNotificationControllerTest/getNotification.json", dto);
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
