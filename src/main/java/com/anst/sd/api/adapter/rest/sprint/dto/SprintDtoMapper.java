package com.anst.sd.api.adapter.rest.sprint.dto;

import com.anst.sd.api.domain.sprint.Sprint;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SprintDtoMapper {
    SprintInfoDto mapToDto(Sprint sprint);

    List<SprintInfoDto> mapToDto(List<Sprint> sprints);
}
