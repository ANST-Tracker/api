package it;

import com.anst.sd.api.adapter.rest.device.dto.DeviceDto;
import com.anst.sd.api.domain.security.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class DeviceControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/device";

    @BeforeEach
    void prepareData() {
        user = createTestUser();
    }

    @Test
    void getDeviceList_successfully() throws Exception {
        createDevice();

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
            .get(API_URL + "/list"))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<DeviceDto> responseDto = getListFromResponse(response, DeviceDto.class);
        responseDto.get(0).setLastLogin(null);
        assertEqualsToFile("/DeviceControllerTest/devices.json", responseDto);
    }

    @Test
    void deleteDevice_successfully() throws Exception {
        Device device = createDevice();

        performAuthenticated(user, MockMvcRequestBuilders
            .delete(API_URL + "/" + device.getId()))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        assertEquals(0, deviceJpaRepository.count());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Device createDevice() {
        Device device = new Device();
        device.setUser(user);
        device.setLastLogin(Instant.now());
        device.setToken("token");
        device.setRemoteAddress("95.79.189.79");
        device.setUserAgent(USER_AGENT);
        return deviceJpaRepository.save(device);
    }
}
