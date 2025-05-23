package com.anst.sd.api.adapter.rest.sprint.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.sprint.Sprint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SprintInfoDtoMapperTest extends AbstractUnitTest {
    private SprintDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SprintDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Sprint sprint = new Sprint();
        sprint
                .setName("SprintName")
                .setDescription("SprintDescription")
                .setProject((Project) new Project().setId(UUID.randomUUID()))
                .setStartDate(LocalDate.now())
                .setEndDate(LocalDate.now().plusDays(1))
                .setIsActive(true)
                .setCreated(Instant.now().minusSeconds(10))
                .setUpdated(Instant.now())
                .setId(UUID.randomUUID());

        SprintInfoDto dto = mapper.mapToInfoDto(sprint);

        assertEquals(sprint.getId(), dto.getId());
        assertEquals(sprint.getName(), dto.getName());
        assertEquals(sprint.getDescription(), dto.getDescription());
        assertEquals(sprint.getStartDate(), dto.getStartDate());
        assertEquals(sprint.getEndDate(), dto.getEndDate());
        assertEquals(sprint.getIsActive(), dto.getIsActive());
    }
}
