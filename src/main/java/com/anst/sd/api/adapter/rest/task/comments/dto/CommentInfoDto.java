package com.anst.sd.api.adapter.rest.task.comments.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentInfoDto {
    UUID id;
    String content;
    Instant created;
    Instant updated;
    UUID authorId;
    String authorName;
}
