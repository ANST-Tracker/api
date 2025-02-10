package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.SimpleDictionary;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SimpleDictionaryDtoMapper {

    List<SimpleDictionaryDto> toDto(List<SimpleDictionary> simpleDictionaryList);
}
