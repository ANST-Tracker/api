package it;

import com.anst.sd.api.adapter.rest.security.dto.JwtResponseDto;
import com.anst.sd.api.adapter.rest.security.dto.LoginRequestDto;
import com.anst.sd.api.adapter.rest.security.dto.RefreshRequestDto;
import com.anst.sd.api.adapter.rest.security.dto.SignupRequestDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.user.User;
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

class AuthControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/auth";

    @Test
    void registerUser_failed_emptyFields() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);
        dto.setEmail(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void registerUser_successfully() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);

        registerUser(dto);

        assertEquals(1, userJpaRepository.findAll().size());
        User registeredUser = userJpaRepository.findAll().get(0);
        assertEquals(dto.getEmail(), registeredUser.getEmail());
        assertEquals(dto.getLastName(), registeredUser.getLastName());
        assertEquals(dto.getFirstName(), registeredUser.getFirstName());
        assertEquals(dto.getUsername(), registeredUser.getUsername());
        List<Project> projectList = projectJpaRepository.findAll();
        assertEquals(1, projectList.size());
        assertEquals(ProjectType.BUCKET, projectList.get(0).getProjectType());
    }

    @Test
    void loginUser_successfully() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);
        registerUser(dto);
        LoginRequestDto loginRequestDto = new LoginRequestDto(dto.getUsername(), dto.getPassword(), UUID.randomUUID());

        MvcResult mvcResult = loginUser(loginRequestDto);

        JwtResponseDto jwtResponseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        assertNotNull(jwtResponseDto.getAccessToken());
        assertNotNull(jwtResponseDto.getRefreshToken());
        assertEquals(1, refreshTokenJpaRepository.findAll().size());
        assertEquals(1, deviceJpaRepository.findAll().size());
    }

    @Test
    void refreshToken_suspiciousActivity() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);
        registerUser(dto);
        LoginRequestDto loginRequestDto = new LoginRequestDto(dto.getUsername(), dto.getPassword(), UUID.randomUUID());
        MvcResult loginResult = loginUser(loginRequestDto);
        JwtResponseDto jwtResponseDto = getFromResponse(loginResult, JwtResponseDto.class);
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto(jwtResponseDto.getRefreshToken());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequestDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private void registerUser(SignupRequestDto signupRequestDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private MvcResult loginUser(LoginRequestDto loginRequestDto) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
