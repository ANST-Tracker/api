package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.domain.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectInfoDtoMapper {
  @Mapping(source = "projectType", target = "type")
  ProjectInfoDto mapToDto(Project source);

  List<ProjectInfoDto> mapToDto(List<Project> source);
}
