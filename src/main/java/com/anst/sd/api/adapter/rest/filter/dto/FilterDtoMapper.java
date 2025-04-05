package com.anst.sd.api.adapter.rest.filter.dto;

import com.anst.sd.api.domain.filter.Filter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilterDtoMapper {
    @Mapping(source = "payload", target = "payload")
    FilterDto mapToDto(Filter source);
}
