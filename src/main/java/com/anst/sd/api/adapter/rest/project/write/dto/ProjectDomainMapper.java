package com.anst.sd.api.adapter.rest.project.write.dto;

import com.anst.sd.api.domain.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectDomainMapper {
    @Mapping(target = "head.id", source = "headId")
    Project mapToDomain(CreateProjectDto source);

    @Mapping(target = "head.id", source = "headId")
    Project mapToDomain(UpdateProjectDto source);
}
