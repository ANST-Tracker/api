package com.anst.sd.api.adapter.rest.sprint;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.sprint.dto.SprintDomainMapper;
import com.anst.sd.api.adapter.rest.sprint.read.dto.CreateSprintDto;
import com.anst.sd.api.adapter.rest.sprint.read.dto.UpdateSprintDto;
import com.anst.sd.api.domain.sprint.Sprint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SprintDomainMapperTest extends AbstractUnitTest {
    private SprintDomainMapper sprintDomainMapper;

    @BeforeEach
    void setUp() {
        sprintDomainMapper = Mappers.getMapper(SprintDomainMapper.class);
    }

    @Test
    void mapToDomain_create() {
        CreateSprintDto createSprintDto = readFromFile("/SprintDomainMapperTest/createDto.json", CreateSprintDto.class);

        Sprint domain = sprintDomainMapper.mapToDomain(createSprintDto);

        assertEqualsToFile("/SprintDomainMapperTest/createSprint.json", domain);
    }

    @Test
    void mapToDomain_update() {
        UpdateSprintDto updateSprintDto = readFromFile("/SprintDomainMapperTest/updateDto.json", UpdateSprintDto.class);

        Sprint domain = sprintDomainMapper.mapToDomain(updateSprintDto);

        assertEqualsToFile("/SprintDomainMapperTest/updateSprint.json", domain);
    }
}
