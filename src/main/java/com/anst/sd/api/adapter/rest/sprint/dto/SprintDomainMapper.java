package com.anst.sd.api.adapter.rest.sprint.dto;

import com.anst.sd.api.adapter.rest.sprint.read.dto.CreateSprintDto;
import com.anst.sd.api.adapter.rest.sprint.read.dto.UpdateSprintDto;
import com.anst.sd.api.domain.sprint.Sprint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SprintDomainMapper {
    @Mapping(target = "project.id", source = "projectId")
    Sprint mapToDomain(CreateSprintDto source);

    Sprint mapToDomain(UpdateSprintDto source);
}
