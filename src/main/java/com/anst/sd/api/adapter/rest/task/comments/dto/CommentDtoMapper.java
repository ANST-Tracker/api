package com.anst.sd.api.adapter.rest.task.comments.dto;

import com.anst.sd.api.domain.task.Comment;
import com.anst.sd.api.domain.user.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {
    @Mapping(source = "author.id", target = "authorId")
    CommentInfoDto mapToDto(Comment comment);

    @AfterMapping
    default void mapToDto(@MappingTarget CommentInfoDto dto, Comment comment) {
        User author = comment.getAuthor();
        dto.setAuthorName("%s %s".formatted(author.getLastName(), author.getFirstName()));
    }
}
