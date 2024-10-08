package com.anst.sd.api.adapter.rest.tag.write.dto;

import com.anst.sd.api.domain.tag.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagDomainMapper {
    Tag mapToDomain(CreateTagDto source);

    Tag mapToDomain(UpdateTagDto source);
}
