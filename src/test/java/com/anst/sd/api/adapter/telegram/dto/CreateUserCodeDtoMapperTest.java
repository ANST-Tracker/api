package com.anst.sd.api.adapter.telegram.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.telegram.dto.CreateUserCodeDto;
import com.anst.sd.api.adapter.telegram.dto.CreateUserCodeDtoMapper;
import com.anst.sd.api.domain.security.UserCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CreateUserCodeDtoMapperTest extends AbstractUnitTest {
    private CreateUserCodeDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CreateUserCodeDtoMapper.class);
    }

    @Test
    void mapToDto() {
        UserCode userCode = readFromFile("/CreateUserCodeDtoMapperTest/userCode.json", UserCode.class);

        CreateUserCodeDto dto = mapper.mapToDto(userCode);

        assertEqualsToFile("/CreateUserCodeDtoMapperTest/userCodeDto.json", dto);
    }
}