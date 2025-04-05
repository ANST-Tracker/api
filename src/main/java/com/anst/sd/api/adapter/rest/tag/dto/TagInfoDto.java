package com.anst.sd.api.adapter.rest.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TagInfoDto {
    @NotNull
    UUID id;
    @NotBlank
    String name;
    @NotNull
    UUID projectId;
}
