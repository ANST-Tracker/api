package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProjectInfoDtoMapperTest extends AbstractUnitTest {

    private ProjectInfoDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProjectInfoDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Project project = readFromFile("/ProjectInfoDtoMapperTest/project.json", Project.class);

        ProjectInfoDto dto = mapper.mapToDto(project);

        assertEqualsToFile("/ProjectInfoDtoMapperTest/projectInfoDto.json", dto);
    }
}