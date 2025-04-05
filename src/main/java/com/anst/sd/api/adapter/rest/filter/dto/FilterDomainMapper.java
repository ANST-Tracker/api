package com.anst.sd.api.adapter.rest.filter.dto;

import com.anst.sd.api.domain.filter.Filter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilterDomainMapper {
    @Mapping(source = "payload", target = "payload")
    Filter mapToDomain(CreateFilterDto source);

    @Mapping(source = "payload", target = "payload")
    Filter mapToDomain(UpdateFilterDto source);
}
