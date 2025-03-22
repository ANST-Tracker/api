package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.tag.dto.TagDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.domain.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

class ProjectInfoDtoMapperTest extends AbstractUnitTest {
    private ProjectInfoDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProjectInfoDtoMapper.class);
        ReflectionTestUtils.setField(mapper, "tagDtoMapper", Mappers.getMapper(TagDtoMapper.class));
        ReflectionTestUtils.setField(mapper, "userDtoMapper", Mappers.getMapper(UserDtoMapper.class));
    }

    @Test
    void mapToDto() {
        Project project = readFromFile("/ProjectInfoDtoMapperTest/project.json", Project.class);

        ProjectInfoDto dto = mapper.mapToDto(project);

        assertEqualsToFile("/ProjectInfoDtoMapperTest/projectInfoDto.json", dto);
    }

    @Test
    void mapToDtoList() {
        List<Project> list = new ArrayList<>();
        for(int i = 0; i < 3;i++){
            list.add(readFromFile("/ProjectInfoDtoMapperTest/project.json", Project.class));
        }

        List<ProjectInfoDto> dto = mapper.mapToDto(list);

        assertEqualsToFile("/ProjectInfoDtoMapperTest/listProjectInfoDto.json", dto);
    }
}