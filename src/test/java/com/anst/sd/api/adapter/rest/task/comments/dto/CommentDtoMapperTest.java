package com.anst.sd.api.adapter.rest.task.comments.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.Comment;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentDtoMapperTest extends AbstractUnitTest {
    private CommentDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CommentDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Comment comment = new Comment();
        comment
                .setAuthor((User) new User()
                        .setFirstName("firstName")
                        .setLastName("lastName")
                        .setId(UUID.randomUUID()))
                .setContent("content")
                .setCreated(Instant.now().minusSeconds(10))
                .setUpdated(Instant.now())
                .setId(UUID.randomUUID());

        CommentInfoDto dto = mapper.mapToDto(comment);

        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getContent(), dto.getContent());
        assertEquals(comment.getCreated(), dto.getCreated());
        assertEquals(comment.getUpdated(), dto.getUpdated());
        assertEquals("lastName firstName", dto.getAuthorName());
        assertEquals(comment.getAuthor().getId(), dto.getAuthorId());
    }
}