package com.anst.sd.api.adapter.rest.project.write.dto;

import com.anst.sd.api.domain.project.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectDomainMapper {
  Project mapToDomain(CreateProjectDto source);

  Project mapToDomain(UpdateProjectDto source);
}
