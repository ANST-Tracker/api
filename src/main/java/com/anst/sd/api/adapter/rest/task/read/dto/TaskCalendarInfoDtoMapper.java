package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskCalendarInfoDtoMapper {
    @Mapping(target = "name", source = "task.data")
    TaskCalendarInfoDto mapToDto(Task task);
}