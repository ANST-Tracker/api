package it;

import com.anst.sd.api.adapter.rest.dto.ErrorInfoDto;
import com.anst.sd.api.adapter.rest.security.dto.*;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.security.UserCode;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static com.anst.sd.api.adapter.rest.dto.ErrorInfoDto.ErrorType.AUTH;
import static com.anst.sd.api.adapter.rest.dto.ErrorInfoDto.ErrorType.CLIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AuthControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/auth";

    @Test
    void loginUser_successfully() throws Exception {
        user = createTestUser();
        LoginRequestDto loginRequestDto = new LoginRequestDto(user.getUsername(), USER_PASSWORD);

        MvcResult mvcResult = loginUser(user.getTelegramId(), loginRequestDto, MockMvcResultMatchers.status().isOk());

        JwtResponseDto jwtResponseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        assertNotNull(jwtResponseDto.getAccessToken());
        assertNotNull(jwtResponseDto.getRefreshToken());
        assertEquals(1, deviceJpaRepository.findAll().size());
        Device device = deviceJpaRepository.findAll().get(0);
        assertNotNull(device.getIp());
    }

    @Test
    void loginUser_failed_differentTelegramId() throws Exception {
        user = createTestUser();
        String differentTgId = "differentId";
        LoginRequestDto loginRequestDto = new LoginRequestDto(user.getUsername(), USER_PASSWORD);

        MvcResult mvcResult = loginUser(differentTgId, loginRequestDto, MockMvcResultMatchers.status().isUnauthorized());

        ErrorInfoDto errorInfoDto = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals(AUTH, errorInfoDto.getType());
    }

    @Test
    void refreshToken_suspiciousActivity() throws Exception {
        user = createTestUser();
        LoginRequestDto loginRequestDto = new LoginRequestDto(user.getUsername(), USER_PASSWORD);
        MvcResult loginResult = loginUser(user.getTelegramId(), loginRequestDto, MockMvcResultMatchers.status().isOk());
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
    void sendCode_successfully_byTelegramId() throws Exception {
        String telegramId = "testId";

        String code = sendGetCodeRequest(telegramId, null);

        assertEquals(5, code.length());
        UserCode userCode = userCodeMongoRepository.findUserCodeByTelegramId(telegramId)
                .orElseThrow();
        assertEquals(telegramId, userCode.getTelegramId());
        assertEquals(code, userCode.getCode());
        verify(createUserCodeMessageSupplier).send(any());
    }

    @Test
    void sendCode_successfully_byUsername() throws Exception {
        user = createTestUser();
        String telegramId = "testId";

        String code = sendGetCodeRequest(telegramId, user.getUsername());

        assertEquals(5, code.length());
        ArgumentCaptor<UserCode> userCodeArgumentCaptor = ArgumentCaptor.forClass(UserCode.class);
        verify(createUserCodeMessageSupplier).send(userCodeArgumentCaptor.capture());
        UserCode userCode = userCodeArgumentCaptor.getValue();
        assertEquals(user.getTelegramId(), userCode.getTelegramId());
    }

    @Test
    void sendCode_failed_codeAlreadySent() throws Exception {
        String telegramId = "testId";
        sendGetCodeRequest(telegramId, null);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/code/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SendCodeRequestDto(telegramId, null))))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        ErrorInfoDto errorResponse = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals(CLIENT, errorResponse.getType());
    }

    @Test
    void verifyCode_successfully_byTelegramId() throws Exception {
        String telegramId = "testId";
        String code = sendGetCodeRequest(telegramId, null);

        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, code, null, MockMvcResultMatchers.status().isOk());

        JwtResponseDto responseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        assertNotNull(responseDto.getAccessToken());
        assertNull(responseDto.getRefreshToken());
    }

    @Test
    void verifyCode_successfully_byUsername() throws Exception {
        user = createTestUser();
        String telegramId = "testId";

        String code = sendGetCodeRequest(telegramId, user.getUsername());

        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, code, user.getUsername(), MockMvcResultMatchers.status().isOk());

        JwtResponseDto responseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        assertNotNull(responseDto.getAccessToken());
        assertNull(responseDto.getRefreshToken());
    }

    @Test
    void verifyCode_failed_expiredCode() throws Exception {
        String telegramId = "testId";
        String code = sendGetCodeRequest(telegramId, null);
        UserCode userCode = userCodeMongoRepository.findAll().get(0);
        userCode.setExpirationTime(Instant.now().minusSeconds(1));
        userCodeMongoRepository.save(userCode);

        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, code, null,
                MockMvcResultMatchers.status().isUnauthorized());

        ErrorInfoDto errorInfoDto = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals(AUTH, errorInfoDto.getType());
    }

    @Test
    void verifyCode_failed_wrongCode() throws Exception {
        String telegramId = "testId";
        sendGetCodeRequest(telegramId, null);

        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, "wrong", null,
                MockMvcResultMatchers.status().isUnauthorized());

        ErrorInfoDto errorInfoDto = getFromResponse(mvcResult, ErrorInfoDto.class);
        assertEquals(AUTH, errorInfoDto.getType());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private ResultActions performTelegramAuthenticated(String telegramId, MockHttpServletRequestBuilder content) throws Exception {
        String token = createToken(telegramId);
        return mockMvc.perform(content.header("Authorization-Telegram", "Bearer " + token));
    }

    private String createToken(String telegramId) throws Exception {
        String code = sendGetCodeRequest(telegramId, null);
        MvcResult mvcResult = sendVerifyCodeRequest(telegramId, code, null,
                MockMvcResultMatchers.status().isOk());
        JwtResponseDto responseDto = getFromResponse(mvcResult, JwtResponseDto.class);
        return responseDto.getAccessToken();
    }

    private String sendGetCodeRequest(String telegramId, String username) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/code/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SendCodeRequestDto(telegramId, username))))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        return getFromResponse(mvcResult, String.class);
    }

    private MvcResult sendVerifyCodeRequest(String telegramId, String code, String username,
                                            ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                        .post(API_URL + "/code/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VerifyCodeRequestDto(telegramId, code, username))))
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
