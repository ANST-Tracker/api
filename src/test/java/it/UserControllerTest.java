package it;

import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.domain.security.Role;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.ERole;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        assertEquals(user.getUsername(), userInfoDto.getUsername());
        assertEquals(List.of(ERole.USER), userInfoDto.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        assertEquals(user.getEmail(), userInfoDto.getEmail());
    }

    @Test
    void getUser_notFound() throws Exception {
        User user = createTestUser();
        userJpaRepository.deleteById(user.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/current"))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteUser_successfully() throws Exception {
        User user = createTestUser();

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/current"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UserInfoDto userInfoDto = getFromResponse(response, UserInfoDto.class);
        assertEquals(user.getFirstName(), userInfoDto.getFirstName());
        assertEquals(user.getLastName(), userInfoDto.getLastName());
        assertEquals(user.getUsername(), userInfoDto.getUsername());
        assertEquals(Set.of(ERole.USER), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        assertEquals(user.getEmail(), userInfoDto.getEmail());
        assertEquals(0, userJpaRepository.findAll().size());
    }

    @Test
    void deleteUser_notFound() throws Exception {
        User user = createTestUser();
        userJpaRepository.deleteById(user.getId());

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL + "/current"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        assertEquals(0, userJpaRepository.findAll().size());
    }
}
