package com.anst.sd.api.adapter.rest.tag.dto;

import com.anst.sd.api.domain.tag.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagDtoMapper {
  TagInfoDto mapToDto(Tag source);

  List<TagInfoDto> mapToDto(List<Tag> source);
}
