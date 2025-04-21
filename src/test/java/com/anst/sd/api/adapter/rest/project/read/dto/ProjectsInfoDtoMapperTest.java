package com.anst.sd.api.adapter.rest.project.read.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDto;
import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDtoMapper;
import com.anst.sd.api.domain.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

class ProjectsInfoDtoMapperTest extends AbstractUnitTest {
    private ProjectsInfoDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProjectsInfoDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Project project = readFromFile("/ProjectInfoDtoMapperTest/project.json", Project.class);

        ProjectsInfoDto dto = mapper.mapToDto(project);

        assertEqualsToFile("/ProjectInfoDtoMapperTest/projectInfoDto.json", dto);
    }

    @Test
    void mapToDtoList() {
        List<Project> list = new ArrayList<>();
        for(int i = 0; i < 3;i++){
            list.add(readFromFile("/ProjectInfoDtoMapperTest/project.json", Project.class));
        }

        List<ProjectsInfoDto> dto = mapper.mapToDto(list);

        assertEqualsToFile("/ProjectInfoDtoMapperTest/listProjectInfoDto.json", dto);
    }
}