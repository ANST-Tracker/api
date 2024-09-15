package com.anst.sd.api.adapter.rest.device.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.device.DeviceInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class DeviceDtoMapperTest extends AbstractUnitTest {
    private DeviceDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(DeviceDtoMapper.class);
    }

    @Test
    void mapToDto() {
        DeviceInfo domain = readFromFile("/DeviceDtoMapperTest/deviceInfo.json", DeviceInfo.class);

        DeviceDto dto = mapper.mapToDto(domain);

        assertEqualsToFile("/DeviceDtoMapperTest/deviceDto.json", dto);
    }
}