package com.anst.sd.api.adapter.rest.sprint;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.sprint.dto.SprintDtoMapper;
import com.anst.sd.api.adapter.rest.sprint.dto.SprintRegistryDto;
import com.anst.sd.api.domain.sprint.Sprint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SprintDtoMapperTest extends AbstractUnitTest {
    private SprintDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SprintDtoMapper.class);
    }

    @Test
    void mapToDto_sprint() {
        Sprint sprint = readFromFile("/SprintDtoMapperTest/sprint.json", Sprint.class);

        SprintRegistryDto dto = mapper.mapToDto(sprint);

        assertEqualsToFile("/SprintDtoMapperTest/sprintDto.json", dto);
    }
}
