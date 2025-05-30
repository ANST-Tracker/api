package it;

import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class UserControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/user";

    @Test
    void getUser_successfully() throws Exception {
        User user = createTestUser();

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/current"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UserInfoDto userInfoDto = getFromResponse(response, UserInfoDto.class);
        assertEquals(user.getFirstName(), userInfoDto.getFirstName());
        assertEquals(user.getLastName(), userInfoDto.getLastName());
        assertEquals(user.getPosition(), userInfoDto.getPosition());
        assertEquals(user.getDepartmentName(), userInfoDto.getDepartmentName());
        assertEquals(user.getTelegramId(), userInfoDto.getTelegramId());
        assertEquals(user.getId(), userInfoDto.getId());
    }

    @Test
    void getUser_notFound() throws Exception {
        User user = createTestUser();
        userJpaRepository.delete(user);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/current"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getUsers_autocomplete_successfully() throws Exception {
        User user = createTestUser();
        createUserWithName("Петр", "Стрельников");
        createUserWithName("Виктор", "Стрельцов");
        createUserWithName("Степан", "Стрелочников");
        createUserWithName("Игорь", "Стрелинский");
        createUserWithName("Юрий", "Стрелов");

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/autocomplete")
                .param("nameFragment", "Стрель"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<UserInfoDto> userInfoDto = getListFromResponse(response, UserInfoDto.class);
        assertEquals(2, userInfoDto.size());
    }
}
