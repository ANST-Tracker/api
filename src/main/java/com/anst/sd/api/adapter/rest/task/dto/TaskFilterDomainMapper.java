package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.filter.Filter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskFilterDomainMapper {
    @Mapping(source = "payload", target = "payload")
    Filter mapToDomain(TaskFilterDto source);
}
