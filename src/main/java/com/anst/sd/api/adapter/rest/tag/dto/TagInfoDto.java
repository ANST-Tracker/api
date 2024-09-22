package com.anst.sd.api.adapter.rest.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TagInfoDto {
    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String color;
}
