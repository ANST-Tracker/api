package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.security.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseDtoMapperTest extends AbstractUnitTest {
    private JwtResponseDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(JwtResponseDtoMapper.class);
    }

    @Test
    void mapToDto() {
        JwtResponse jwtResponse = readFromFile("/JwtResponseDtoMapperTest/jwtResponse.json", JwtResponse.class);

        JwtResponseDto dto = mapper.mapToDto(jwtResponse);

        assertEqualsToFile("/JwtResponseDtoMapperTest/jwtResponseDto.json", dto);
    }
}