package com.anst.sd.api.adapter.rest.task.log.dto;

import com.anst.sd.api.domain.task.Log;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LogDomainMapper {
    Log mapToDomain(CreateUpdateLogDto source);
}
