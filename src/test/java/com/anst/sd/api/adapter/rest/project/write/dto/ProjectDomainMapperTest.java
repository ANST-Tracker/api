package com.anst.sd.api.adapter.rest.project.write.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProjectDomainMapperTest extends AbstractUnitTest {
    private ProjectDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProjectDomainMapper.class);
    }

    @Test
    void mapToDomain_create() {
        CreateProjectDto dto = readFromFile("/ProjectDomainMapperTest/createDto.json", CreateProjectDto.class);

        Project project = mapper.mapToDomain(dto);

        assertEqualsToFile("/ProjectDomainMapperTest/project.json", project);
    }

    @Test
    void mapToDomain_update() {
        UpdateProjectDto dto = readFromFile("/ProjectDomainMapperTest/updateDto.json", UpdateProjectDto.class);

        Project project = mapper.mapToDomain(dto);

        assertEqualsToFile("/ProjectDomainMapperTest/project.json", project);
    }
}