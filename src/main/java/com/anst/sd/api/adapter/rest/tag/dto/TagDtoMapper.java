package com.anst.sd.api.adapter.rest.tag.dto;

import com.anst.sd.api.domain.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagDtoMapper {
    @Mapping(target = "projectId", source = "project.id")
    TagInfoDto mapToDto(Tag source);

    @Mapping(target = "projectId", source = "project.id")
    List<TagInfoDto> mapToDto(List<Tag> source);
}
