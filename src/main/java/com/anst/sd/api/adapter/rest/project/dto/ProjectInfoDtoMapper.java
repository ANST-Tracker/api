package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.domain.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectInfoDtoMapper {
    @Mapping(target = "headId", source = "head.id")
    ProjectInfoDto mapToDto(Project source);

    @Mapping(target = "headId", source = "head.id")
    List<ProjectInfoDto> mapToDto(List<Project> source);
}
