package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.AbstractTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbstractTaskDtoMapper {
    IdResponseDto mapToDto(AbstractTask source);
}
