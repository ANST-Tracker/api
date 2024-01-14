package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserDtoMapperTest extends AbstractUnitTest {
    private UserDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserDtoMapper.class);
    }

    @Test
    void mapToDto() {
        User source = readFromFile("/UserDtoMapperTest/user.json", User.class);

        UserInfoDto dto = mapper.mapToDto(source);

        assertEqualsToFile("/UserDtoMapperTest/userDto.json", dto);
    }
}
