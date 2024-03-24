package it;

import com.anst.sd.api.adapter.rest.dto.ErrorInfoDto;
import com.anst.sd.api.adapter.rest.security.dto.*;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.security.UserCode;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AuthControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/auth";

    @Test
    void registerUser_failed_emptyFields() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);
        String telegramId = dto.getTelegramId();
        dto.setTelegramId(null);

        registerUser(telegramId, dto, MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void registerUser_failed_differentTelegramIds() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);
        final String userTgId = dto.getTelegramId();
        final String differentTgId = "differentId";

        MvcResult mvcResult = registerUser(differentTgId, dto, MockMvcResultMatchers.status().isBadRequest());

        ErrorInfoDto errorInfoDto = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals("Can't register user. Reason Trying to register user with telegramId %s by token for another id %s"
                .formatted(userTgId, differentTgId), errorInfoDto.getMessage());
    }

    @Test
    void registerUser_successfully() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);

        registerUser(dto.getTelegramId(), dto, MockMvcResultMatchers.status().isOk());

        assertEquals(1, userJpaRepository.findAll().size());
        User registeredUser = userJpaRepository.findAll().get(0);
        assertEquals(dto.getTelegramId(), registeredUser.getTelegramId());
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
        registerUser(dto.getTelegramId(), dto, MockMvcResultMatchers.status().isOk());
        LoginRequestDto loginRequestDto = new LoginRequestDto(dto.getUsername(), dto.getPassword(), UUID.randomUUID());

        MvcResult mvcResult = loginUser(dto.getTelegramId(), loginRequestDto, MockMvcResultMatchers.status().isOk());

        JwtResponseDto jwtResponseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        assertNotNull(jwtResponseDto.getAccessToken());
        assertNotNull(jwtResponseDto.getRefreshToken());
        assertEquals(1, refreshTokenJpaRepository.findAll().size());
        assertEquals(1, deviceJpaRepository.findAll().size());
    }

    @Test
    void loginUser_failed_differentTelegramId() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);
        final String userTgId = dto.getTelegramId();
        final String differentTgId = "differentId";
        registerUser(userTgId, dto, MockMvcResultMatchers.status().isOk());
        LoginRequestDto loginRequestDto = new LoginRequestDto(dto.getUsername(), dto.getPassword(), UUID.randomUUID());

        MvcResult mvcResult = loginUser(differentTgId, loginRequestDto, MockMvcResultMatchers.status().isUnauthorized());

        ErrorInfoDto errorInfoDto = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals("Trying to login user with tgId %s with token for tgId %s".formatted(userTgId, differentTgId),
            errorInfoDto.getMessage());
    }

    @Test
    void refreshToken_suspiciousActivity() throws Exception {
        SignupRequestDto dto = readFromFile("/AuthControllerTest/registerUserDto.json", SignupRequestDto.class);
        registerUser(dto.getTelegramId(), dto, MockMvcResultMatchers.status().isOk());
        LoginRequestDto loginRequestDto = new LoginRequestDto(dto.getUsername(), dto.getPassword(), UUID.randomUUID());
        MvcResult loginResult = loginUser(dto.getTelegramId(), loginRequestDto, MockMvcResultMatchers.status().isOk());
        JwtResponseDto jwtResponseDto = getFromResponse(loginResult, JwtResponseDto.class);
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto(jwtResponseDto.getRefreshToken());

        mockMvc.perform(MockMvcRequestBuilders
                .post(API_URL + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequestDto)))
            .andDo(print())

            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void sendCode_successfully() throws Exception {
        String telegramId = "testId";

        String code = sendGetCodeRequest(telegramId);

        assertEquals(5, code.length());
        verify(createUserCodeMessageSupplier).send(any());
    }

    @Test
    void sendCode_failed_codeAlreadySent() throws Exception {
        String telegramId = "testId";
        sendGetCodeRequest(telegramId);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(API_URL + "/code/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SendCodeRequestDto(telegramId))))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

        ErrorInfoDto errorResponse = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals("Code already sent to telegramId " + telegramId, errorResponse.getMessage());
    }

    @Test
    void verifyCode_successfully() throws Exception {
        String telegramId = "testId";
        String code = sendGetCodeRequest(telegramId);

        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, code, MockMvcResultMatchers.status().isOk());

        JwtResponseDto responseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        assertNotNull(responseDto.getAccessToken());
        assertNull(responseDto.getRefreshToken());
    }

    @Test
    void verifyCode_failed_expiredCode() throws Exception {
        String telegramId = "testId";
        String code = sendGetCodeRequest(telegramId);
        UserCode userCode = userCodeMongoRepository.findAll().get(0);
        userCode.setExpirationTime(Instant.now().minusSeconds(1));
        userCodeMongoRepository.save(userCode);

        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, code, MockMvcResultMatchers.status().isUnauthorized());

        ErrorInfoDto errorInfoDto = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals("Code is expired", errorInfoDto.getMessage());
    }

    @Test
    void verifyCode_failed_wrongCode() throws Exception {
        String telegramId = "testId";
        sendGetCodeRequest(telegramId);

        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, "wrong", MockMvcResultMatchers.status().isUnauthorized());

        ErrorInfoDto errorInfoDto = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals("Wrong code", errorInfoDto.getMessage());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private ResultActions performTelegramAuthenticated(String telegramId, MockHttpServletRequestBuilder content) throws Exception {
        String token = createToken(telegramId);
        return mockMvc.perform(content.header("Authorization-Telegram", "Bearer " + token));
    }

    private String createToken(String telegramId) throws Exception {
        String code = sendGetCodeRequest(telegramId);
        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, code, MockMvcResultMatchers.status().isOk());
        JwtResponseDto responseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        return responseDto.getAccessToken();
    }

    private String sendGetCodeRequest(String telegramId) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(API_URL + "/code/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SendCodeRequestDto(telegramId))))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        return getFromResponse(mvcResult, String.class);
    }

    private MvcResult sendVerifyCodeRequest(String telegramId, String code, ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(API_URL + "/code/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VerifyCodeRequestDto(telegramId, code))))
            .andDo(print())
            .andExpect(resultMatcher)
            .andReturn();
    }

    private MvcResult registerUser(String telegramId, SignupRequestDto signupRequestDto, ResultMatcher resultMatcher) throws Exception {
        return performTelegramAuthenticated(telegramId, MockMvcRequestBuilders
                .post(API_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
            .andDo(print())
            .andExpect(resultMatcher)
            .andReturn();
    }

    private MvcResult loginUser(String telegramId, LoginRequestDto loginRequestDto, ResultMatcher resultMatcher) throws Exception {
        return performTelegramAuthenticated(telegramId, MockMvcRequestBuilders
                .post(API_URL + "/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
            .andDo(print())
            .andExpect(resultMatcher)
            .andReturn();
    }
}
