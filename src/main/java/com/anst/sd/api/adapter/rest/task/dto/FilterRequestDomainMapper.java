package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.app.api.task.FilterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilterRequestDomainMapper {
    FilterRequest mapToDomain(FilterRequestDto filterRequestDto);
}