package com.anst.sd.api.adapter.rest.project.read.dto;

import com.anst.sd.api.domain.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectsInfoDtoMapper {
    @Mapping(target = "headId", source = "head.id")
    ProjectsInfoDto mapToDto(Project source);

    @Mapping(target = "headId", source = "head.id")
    List<ProjectsInfoDto> mapToDto(List<Project> source);
}
