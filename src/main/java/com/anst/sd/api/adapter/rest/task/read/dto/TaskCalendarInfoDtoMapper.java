package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskCalendarInfoDtoMapper {
    TaskCalendarInfoDto mapToDto(Task task);
}