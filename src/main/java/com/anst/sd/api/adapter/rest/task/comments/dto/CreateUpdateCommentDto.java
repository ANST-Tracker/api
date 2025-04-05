package com.anst.sd.api.adapter.rest.task.comments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUpdateCommentDto {
    @NotBlank
    String content;
}
