package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.dto.SimpleDictionaryDto;
import com.anst.sd.api.domain.task.SimpleDictionary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleDictionaryDtoMapper {
    SimpleDictionaryDto toDto(SimpleDictionary simpleDictionary);
}
