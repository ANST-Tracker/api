package com.anst.sd.api.adapter.rest.tag.write.dto;

import com.anst.sd.api.domain.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagDomainMapper {
    @Mapping(target = "project.id", source = "projectId")
    Tag mapToDomain(CreateTagDto source);
}
