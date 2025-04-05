package com.anst.sd.api.adapter.rest.task.comments.dto;

import com.anst.sd.api.domain.task.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {
    CommentInfoDto mapToDto(Comment comment);
}
