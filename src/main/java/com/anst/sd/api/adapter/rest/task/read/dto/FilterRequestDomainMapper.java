package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.app.api.task.TaskFilter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilterRequestDomainMapper {
    TaskFilter mapToDomain(TaskFilterRequestDto taskFilterRequestDto);
}
